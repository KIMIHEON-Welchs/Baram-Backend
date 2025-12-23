package com.example.Baram.domain.record;

import com.example.Baram.domain.feedback.Feedback;
import com.example.Baram.domain.feedback.FeedbackRepository;
import com.example.Baram.domain.feedback.dto.FeedbackResponse;
import com.example.Baram.domain.model.AnswerModel;
import com.example.Baram.domain.model.AnswerModelRepository; // (없으면 만들어야 함)
import com.example.Baram.domain.record.dto.RecordResponse;
import com.example.Baram.domain.record.dto.RecordSubmitRequest;
import com.example.Baram.domain.user.User;
import com.example.Baram.domain.user.UserRepository;
import com.example.Baram.domain.record.RecordSearchRepository;
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
    public Long submitRecord(Long userId, RecordSubmitRequest request, String fileName) {

        // 1. 유저 확인 (여전히 DB 조회가 필요합니다)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. [변경] Sentence, Model 조회 로직 삭제
        // - 문장은 request에서 직접 String으로 가져옵니다.
        // - 모델은 request의 font명을 그대로 사용합니다. [cite: 2025-12-23]

        AnswerModel model = modelRepository.findByModelName(request.getFont())
                .orElseThrow(() -> new IllegalArgumentException("해당 폰트 모델을 찾을 수 없습니다: " + request.getFont()));

        // 3. Record 엔티티 생성 및 저장
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

        // 4. [변경 예정] 상세 분석 로직 (Feedback 4분할 반영 필요)
        // - 이 부분은 곧 만드실 Shape, Position, Spacing, TiltFeedback 엔티티로 쪼개야 합니다. [cite: 2025-12-23]
        // - 지금은 임시로 기존 Mock 로직을 유지하거나 주석 처리합니다.
        processMockAnalysis(savedRecord);

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


    // [AI 연동 시 변경 포인트]
    // 지금은 이 메소드가 내부에서 가짜 점수를 만들지만,
    // 나중에는 "Python AI 서버"로 이미지를 보내고 결과를 받아오는 코드로 바뀝니다.
    private Feedback processMockAnalysis(Record record)
    {
        // =============================================================
        // [TODO: AI 서버 연동 시 삭제할 구간 START]
        // -------------------------------------------------------------

        // (1) 가짜 랜덤 점수 로직 (삭제 예정)
        float randomScore = (float) (Math.random() * 50) + 50;
        randomScore = Math.round(randomScore * 100) / 100.0f;

        // (2) 가짜 피드백 텍스트 (삭제 예정)
        String mockTip = "조금 더 천천히 써보세요!";
        String mockJamo = "자음 모양이 아주 좋습니다.";
        // -------------------------------------------------------------
        // [TODO: AI 서버 연동 시 삭제할 구간 END]
        // =============================================================


		/* [TODO: 나중에 들어갈 진짜 코드 예시]

		// 1. AI 서버로 요청 보내기 (RestTemplate 사용)
		AiResponseDto aiResult = aiClient.analyzeImage(record.getSubmittedImageUrl());

		// 2. 받아온 결과 변수에 넣기
		float randomScore = aiResult.getScore();
		String mockTip = aiResult.getFeedback();
		String mockJamo = aiResult.getJamoAnalysis();

		*/

        // (3) 결과 DB 반영 (이 부분은 유지됨, 변수만 진짜로 교체)
        record.setFinalScore(randomScore);
        record.setAnalysisStatus(AnalysisStatus.COMPLETED);
        record.setRecognizedTextHtr(record.getSentence());

        recordRepository.save(record);

        Feedback feedback = Feedback.builder()
                .record(record)
                .analysis1JamoShape(mockJamo) // 가짜 변수 -> 진짜 변수로 교체
                .improvementTip(mockTip)      // 가짜 변수 -> 진짜 변수로 교체
                .build();

        return feedbackRepository.save(feedback);
    }

    // domain.record.RecordService.java

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

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final AnswerModelRepository modelRepository;
    private final FeedbackRepository feedbackRepository;
    private final RecordSearchRepository recordSearchRepository;
}