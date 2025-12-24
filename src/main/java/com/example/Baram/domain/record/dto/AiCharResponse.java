package com.example.Baram.domain.record.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * AI 서버의 글자 분리(Segmentation) 분석 결과를 매핑하기 위한 DTO입니다.
 * AI 서버로부터 전달받는 Base64 이미지 데이터와 문장 구조를 담습니다.
 */
@Getter @NoArgsConstructor
public class AiCharResponse {
    private String sentence;
    private List<CharItem> items;

    @Getter @NoArgsConstructor
    public static class CharItem {
        @JsonProperty("char") private String chr;
        private String image;
        private JamoImageItems items;
    }

    @Getter @NoArgsConstructor
    @SuppressWarnings("unused")
    public static class JamoImageItems {
        private ImageWrapper chosung, jungsung, jongsung;
    }

    @Getter @NoArgsConstructor
    public static class ImageWrapper {
        private String image; // Base64 문자열
    }
}