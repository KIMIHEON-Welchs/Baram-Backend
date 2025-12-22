package com.example.Baram.domain.sentence.dto;

import com.example.Baram.domain.sentence.SentenceLibrary;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SentenceResponse {
    private Long sentenceId;
    private String content;
    private String category;
    private String level;

    public static SentenceResponse from(SentenceLibrary sentence) {
        return SentenceResponse.builder()
                .sentenceId(sentence.getSentenceId())
                .content(sentence.getContent())
                .category(sentence.getCategory())
                .level(sentence.getLevel().name()) // Enum을 String으로 변환
                .build();
    }
}