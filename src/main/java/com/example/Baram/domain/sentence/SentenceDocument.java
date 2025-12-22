package com.example.Baram.domain.sentence;

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

@Document(indexName = "sentence_library")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentenceDocument
{
    // --- Methods ---

    // --- Member Variables (Rule 2.2: 하단 배치) ---

    @Id
    private String id; // ES는 ID를 String으로 관리

    @Field(type = FieldType.Text, analyzer = "standard")
    private String content; // 문장 내용 (검색 대상)

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Keyword)
    private String level;

    @Field(type = FieldType.Boolean)
    private Boolean isActive;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;
}