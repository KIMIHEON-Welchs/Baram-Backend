package com.example.Baram.domain.record;

import com.example.Baram.domain.model.AnswerModel;
import com.example.Baram.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    // --- 관계 매핑 (Foreign Keys) ---

    // 한 명의 유저가 여러 기록을 가짐
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // 하나의 채점 모델로 여러 기록을 채점함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_version_id", nullable = false)
    private AnswerModel answerModel;

    // --- 일반 필드 ---
    @Column(columnDefinition = "TEXT")
    private String sentence;

    @Column(nullable = false)
    private String submittedImageUrl; // 업로드된 이미지 경로(S3 URL 등)

    @Column(columnDefinition = "TEXT")
    private String recognizedTextHtr; // AI가 읽어낸 텍스트 (HTR 결과)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String confirmedTextFinal; // 사용자가 확인/수정한 최종 텍스트

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionMethod submissionMethod;

    private Float finalScore; // 최종 점수 (소수점 가능)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnalysisStatus analysisStatus;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime submissionDate;
}