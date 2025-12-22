package com.example.Baram.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerModel
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_version_id")
    private Long modelVersionId;

    @Column(length = 100, nullable = false)
    private String modelName; // 예: "v1.0.0_basic_scoring"

    @Column(columnDefinition = "TEXT")
    private String description; // 모델에 대한 설명

    // ERD에 'JamoReferenceData_JSON'이라고 된 부분
    // 실제 DB에는 TEXT 타입으로 저장하고, 사용할 때는 JSON 문자열을 넣습니다.
    @Column(columnDefinition = "TEXT", nullable = false)
    private String jamoReferenceDataJson;

    // ERD에 'SpacingRules_JSON'이라고 된 부분
    @Column(columnDefinition = "TEXT", nullable = false)
    private String spacingRulesJson;

    @Column(nullable = false)
    private Boolean isActive; // 현재 활성화된 채점 모델인지 여부

    @Column(nullable = false)
    private LocalDateTime releaseDate; // 모델 배포일
}