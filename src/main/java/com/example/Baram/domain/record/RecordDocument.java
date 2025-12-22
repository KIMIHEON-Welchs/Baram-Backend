package com.example.Baram.domain.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "record_history") // 인덱스 이름(소문자)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordDocument
{
    // --- Methods ---

    // --- Member Variables ---

    @Id
    private String id; // DB의 recordId를 문자열로 저장

    @Field(type = FieldType.Long)
    private Long userId; // 누가 썼는지 (검색 필터용)

    @Field(type = FieldType.Text, analyzer = "standard")
    private String sentenceContent; // 문장 내용 (검색용)

    @Field(type = FieldType.Text)
    private String recognizedText; // AI가 인식한 텍스트

    @Field(type = FieldType.Text)
    private String feedbackTip; // 피드백 조언 내용 (Feedback 테이블 내용을 여기에 합침!)

    @Field(type = FieldType.Float)
    private Float score;

    @Field(type = FieldType.Date)
    private LocalDateTime submittedAt;
}