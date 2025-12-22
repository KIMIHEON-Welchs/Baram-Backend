package com.example.Baram.domain.feedback;

import com.example.Baram.domain.record.Record;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    // 하나의 기록(Record)에 대해 하나의 피드백이 생성됨 (1:1 관계)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private Record record;

    // 분석 데이터들 (JSON 대신 TEXT로 저장)
    @Column(columnDefinition = "TEXT")
    private String analysis1JamoShape;       // 자음 모양 분석

    @Column(columnDefinition = "TEXT")
    private String analysis2JamoSpacing;     // 자음 간격 분석

    @Column(columnDefinition = "TEXT")
    private String analysis3LetterSpacing;   // 글자 간격 분석

    @Column(columnDefinition = "TEXT")
    private String analysis4SentenceAlignment; // 문장 정렬 분석

    @Column(columnDefinition = "TEXT")
    private String improvementTip;           // AI가 주는 개선 팁
}