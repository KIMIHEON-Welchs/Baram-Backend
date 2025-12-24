package com.example.Baram.domain.record.dto;

import com.example.Baram.domain.record.Record;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 필기 분석 기록의 요약 정보를 전달하는 데이터 전송 객체(DTO)입니다.
 * 주로 분석 내역 목록 조회 시 사용됩니다.
 */
@Getter
@Builder
public class RecordResponse {
    private Long recordId;
    private String sentenceContent; // 연습한 문장 내용
    private float finalScore;
    private LocalDateTime submissionDate;
    private String imageUrl;

    public static RecordResponse from(Record record) {
        return RecordResponse.builder()
                .recordId(record.getRecordId())
                .sentenceContent(record.getSentence())
                .finalScore(record.getFinalScore())
                .submissionDate(record.getSubmissionDate())
                .imageUrl(record.getSubmittedImageUrl())
                .build();
    }
}