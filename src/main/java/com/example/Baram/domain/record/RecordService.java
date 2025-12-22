package com.example.Baram.domain.record;

import com.example.Baram.domain.feedback.Feedback;
import com.example.Baram.domain.feedback.FeedbackRepository;
import com.example.Baram.domain.feedback.dto.FeedbackResponse;
import com.example.Baram.domain.model.AnswerModel;
import com.example.Baram.domain.model.AnswerModelRepository; // (없으면 만들어야 함)
import com.example.Baram.domain.record.dto.RecordResponse;
import com.example.Baram.domain.record.dto.RecordSubmitRequest;
import com.example.Baram.domain.sentence.SentenceLibrary;
import com.example.Baram.domain.sentence.SentenceLibraryRepository; // (없으면 만들어야 함)
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
    public Long submitRecord(MultipartFile file, RecordSubmitRequest request) throws IOException {

        // 1. 이미지 파일 로컬에 저장하기
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files"; // 저장할 경로
        UUID uuid = UUID.randomUUID(); // 파일명 겹치지 않게 랜덤 이름 생성
        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs(); // 폴더 없으면 생성
        }
        file.transferTo(saveFile); // 실제 파일 저장 실행

        // 2. DB 조회 (유저, 문장, 모델 있는지 확인)
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("없는 유저입니다."));
        SentenceLibrary sentence = sentenceRepository.findById(request.getSentenceId())
                .orElseThrow(() -> new IllegalArgumentException("없는 문장입니다."));
        AnswerModel model = modelRepository.findById(request.getModelVersionId())
                .orElseThrow(() -> new IllegalArgumentException("없는 모델입니다."));

        // 3. Record 엔티티 생성 및 저장
        Record record = Record.builder()
                .user(user)
                .sentence(sentence)
                .answerModel(model)
                .submittedImageUrl("/files/" + fileName) // 나중에 접근할 URL 경로
                .submissionMethod(request.getSubmissionMethod())
                .analysisStatus(AnalysisStatus.PENDING) // 일단 '대기중'으로 저장
                .confirmedTextFinal("") // 아직 분석 전이라 빈 값
                .build();

        Record savedRecord = recordRepository.save(record);

        Feedback feedback = processMockAnalysis(savedRecord);

        RecordDocument esDoc = RecordDocument.builder()
                .id(savedRecord.getRecordId().toString()) // DB ID와 동기화
                .userId(user.getUserId())
                .sentenceContent(sentence.getContent()) // 문장 내용 복사
                .recognizedText(savedRecord.getRecognizedTextHtr())
                .feedbackTip(feedback.getImprovementTip()) // 피드백 내용 복사
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
        record.setRecognizedTextHtr(record.getSentence().getContent());

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
    private final SentenceLibraryRepository sentenceRepository;
    private final AnswerModelRepository modelRepository;
    private final FeedbackRepository feedbackRepository;
    private final  RecordSearchRepository recordSearchRepository;
}