package com.example.Baram.domain.record;

import com.example.Baram.domain.feedback.Feedback;
import com.example.Baram.domain.feedback.FeedbackRepository;
import com.example.Baram.domain.feedback.dto.FeedbackResponse;
import com.example.Baram.domain.model.AnswerModel;
import com.example.Baram.domain.model.AnswerModelRepository; // (없으면 만들어야 함)
import com.example.Baram.domain.record.dto.AiCharResponse;
import com.example.Baram.domain.record.dto.RecordAnalysisResponse;
import com.example.Baram.domain.record.dto.RecordResponse;
import com.example.Baram.domain.record.dto.RecordSubmitRequest;
import com.example.Baram.domain.user.User;
import com.example.Baram.domain.user.UserRepository;
import com.example.Baram.domain.record.RecordSearchRepository;
import com.example.Baram.domain.record.dto.AiAnalysisResponse;
import com.example.Baram.domain.record.AiClientService;
import com.example.Baram.global.util.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService
{

    @Transactional
    // 컨트롤러에서 토큰을 통해 추출한 userId를 파라미터로 넘겨준다고 가정합니다.
    public Long submitRecord(Long userId, RecordSubmitRequest request, MultipartFile file, String fileName) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        AnswerModel model = modelRepository.findByModelName(request.getFont())
                .orElseThrow(() -> new IllegalArgumentException("해당 폰트 모델을 찾을 수 없습니다: " + request.getFont()));

        //  Record 엔티티 생성 및 저장
        Record record = Record.builder()
                .user(user)
                .answerModel(model)          // [변경] modelVersionId 대신 font 저장
                .sentence(request.getSentence())  // [변경] sentenceId 대신 직접 텍스트 저장
                .submittedImageUrl("/files/" + fileName)
                .submissionMethod(SubmissionMethod.CANVAS) // "직접 그리기" 고정 또는 request 활용
                .analysisStatus(AnalysisStatus.PENDING)
                .confirmedTextFinal("")
                .build();

        Record savedRecord = recordRepository.save(record);

        processAiAnalysis(savedRecord, file, request.getFont());

        // 5. Elasticsearch 동기화 (RecordDocument)
        RecordDocument esDoc = RecordDocument.builder()
                .id(savedRecord.getRecordId().toString())
                .userId(user.getUserId())
                .sentenceContent(savedRecord.getSentence()) // [변경] 엔티티에서 직접 텍스트 복사
                .recognizedText(savedRecord.getRecognizedTextHtr())
                // 피드백 팁은 4분할된 피드백 중 대표 팁을 넣거나 통합 로직 필요 [cite: 2025-12-18, 2025-12-23]
                .feedbackTip("분석 결과가 생성되었습니다.")
                .score(savedRecord.getFinalScore())
                .submittedAt(savedRecord.getSubmissionDate())
                .build();

        recordSearchRepository.save(esDoc);

        return savedRecord.getRecordId();
    }


    private void processAiAnalysis(Record record, MultipartFile file, String fontName) {
        try {
            AiAnalysisResponse aiResult = aiClientService.sendToAnalysis(file, fontName);

            // (2) 메인 Record 테이블 업데이트 (전체 점수 및 상태) [cite: 2025-12-24]
            record.setFinalScore(aiResult.getFinalScore());
            record.setAnalysisStatus(AnalysisStatus.COMPLETED);
            record.setRecognizedTextHtr(aiResult.getSentence());
            recordRepository.save(record);

            // (3) 상세 분석 결과(RecordDetail) 테이블 저장 [cite: 2025-12-24]
            AiAnalysisResponse.AnalysedData analysed = aiResult.getAnalysed();

            RecordDetail detail = RecordDetail.builder()
                    .record(record)
                    .tiltScore(analysed.getTilt().getTiltScore())
                    .spaceScore(analysed.getSpace().getSpaceScore())
                    .sizeScore(analysed.getSize().getSizeScore())
                    .jamoPositionScore(analysed.getJamoPosition().getJamoPositionScore())
                    .jamoShapeScore(analysed.getJamoShape().getJamoShapeScore())
                    // analysed 객체 전체(좌표 포함)를 JSONB 컬럼에 저장 [cite: 2025-12-24]
                    .detailedJson(objectMapper.writeValueAsString(analysed))
                    .build();

            recordDetailRepository.save(detail);
        } catch (Exception e) {
            record.setAnalysisStatus(AnalysisStatus.FAILED);
            recordRepository.save(record);
            throw new RuntimeException("AI 분석 처리 중 서버 오류가 발생했습니다.", e);
        }
    }

    private void processSegmentationImages(Record record, MultipartFile file) {
        try {
            // 1. AI 서버에서 char.json 데이터 수신 [cite: 2025-12-24]
            AiCharResponse charResponse = aiClientService.sendToSegmentation(file);

            List<AiCharResponse.CharItem> items = charResponse.getItems();

            // 2. 각 글자(Segment)별로 반복 처리 [cite: 2025-12-24]
            for (int i = 0; i < items.size(); i++) {
                AiCharResponse.CharItem item = items.get(i);

                // 3. FileUtil을 사용해 각 이미지를 실제 파일로 저장 [cite: 2025-12-18, 2025-12-24]
                // recordId별로 폴더를 나누어 저장합니다.
                String wholePath = fileUtil.saveBase64Image(record.getRecordId(), "whole", item.getImage());

                AiCharResponse.JamoImageItems jamoItems = item.getItems();
                String choPath = fileUtil.saveBase64Image(record.getRecordId(), "chosung", jamoItems.getChosung().getImage());
                String jungPath = fileUtil.saveBase64Image(record.getRecordId(), "jungsung", jamoItems.getJungsung().getImage());

                // 종성은 없을 수 있으므로 null 체크를 수행합니다. [cite: 2025-12-24]
                String jongPath = null;
                if (jamoItems.getJongsung() != null) {
                    jongPath = fileUtil.saveBase64Image(record.getRecordId(), "jongsung", jamoItems.getJongsung().getImage());
                }

                // 4. 저장된 경로들을 RecordSegment 엔티티로 만들어 DB에 저장 [cite: 2025-12-24]
                RecordSegment segment = RecordSegment.builder()
                        .record(record)
                        .charIndex(i) // 몇 번째 글자인지 인덱스 기록 [cite: 2025-12-24]
                        .chr(item.getChr())
                        .wholePath(wholePath)
                        .chosungPath(choPath)
                        .jungsungPath(jungPath)
                        .jongsungPath(jongPath)
                        .build();

                recordSegmentRepository.save(segment);
            }
        } catch (IOException e) {
            throw new RuntimeException("자모 이미지 저장 중 오류가 발생했습니다.", e);
        }
    }

    // 사용자의 전체 기록 리스트 가져오기
    @Transactional(readOnly = true)
    public List<RecordResponse> getUserRecords(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return recordRepository.findByUserOrderBySubmissionDateDesc(user).stream()
                .map(RecordResponse::from) // Entity -> DTO 변환
                .collect(Collectors.toList());
    }

    // 특정 기록의 상세 피드백 가져오기
    @Transactional(readOnly = true)
    public FeedbackResponse getFeedbackByRecordId(Long recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기록입니다."));

        Feedback feedback = feedbackRepository.findByRecord(record)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록에 대한 피드백이 없습니다."));

        return FeedbackResponse.from(feedback); // Entity -> DTO 변환
    }

    @Transactional(readOnly = true)
    public RecordAnalysisResponse getDetailedAnalysis(Long recordId) {
        // 1. 상세 점수 및 JSON 데이터 조회 [cite: 2025-12-24]
        RecordDetail detail = recordDetailRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록의 상세 분석 데이터가 없습니다. ID: " + recordId));

        // 2. 자모 분리 이미지 경로 리스트 조회 (인덱스 순서대로 정렬) [cite: 2025-12-24]
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기록입니다."));

        List<RecordSegment> segments = recordSegmentRepository.findByRecordOrderByCharIndexAsc(record);

        // 3. detailedJson(String)을 JSON 객체(Object)로 변환 [cite: 2025-12-24]
        Object jsonNode;
        try {
            jsonNode = objectMapper.readTree(detail.getDetailedJson());
        } catch (Exception e) {
            jsonNode = detail.getDetailedJson(); // 변환 실패 시 문자열 그대로 전달 [cite: 2025-12-24]
        }

        // 4. DTO 변환 및 반환 [cite: 2025-12-24]
        return RecordAnalysisResponse.builder()
                .tiltScore(detail.getTiltScore())
                .spaceScore(detail.getSpaceScore())
                .sizeScore(detail.getSizeScore())
                .jamoPositionScore(detail.getJamoPositionScore())
                .jamoShapeScore(detail.getJamoShapeScore())
                .detailedJson(jsonNode)
                .segments(segments.stream()
                        .map(s -> RecordAnalysisResponse.SegmentResponse.builder()
                                .charIndex(s.getCharIndex())
                                .character(s.getChr())
                                .wholePath(s.getWholePath())
                                .chosungPath(s.getChosungPath())
                                .jungsungPath(s.getJungsungPath())
                                .jongsungPath(s.getJongsungPath())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final AnswerModelRepository modelRepository;
    private final FeedbackRepository feedbackRepository;
    private final RecordSearchRepository recordSearchRepository;
    private final RecordDetailRepository recordDetailRepository;
    private final AiClientService aiClientService;
    private final RecordSegmentRepository recordSegmentRepository;
    private final FileUtil fileUtil;
    private final ObjectMapper objectMapper;
}