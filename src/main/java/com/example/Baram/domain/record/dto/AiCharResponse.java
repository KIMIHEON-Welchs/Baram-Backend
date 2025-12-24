package com.example.Baram.domain.record.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter @NoArgsConstructor
public class AiCharResponse {
    private String sentence;
    private List<CharItem> items;

    @Getter @NoArgsConstructor
    public static class CharItem {
        @JsonProperty("char") private String chr; // 수정 완료 [cite: 2025-12-24]
        private String image;
        private JamoImageItems items;
    }

    @Getter @NoArgsConstructor
    public static class JamoImageItems {
        private ImageWrapper chosung, jungsung, jongsung;
    }

    @Getter @NoArgsConstructor
    public static class ImageWrapper {
        private String image; // Base64 문자열
    }
}