package com.example.Baram.domain.record.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter @NoArgsConstructor
public class AiAnalysisResponse {
    private String sentence;
    private String font;
    @JsonProperty("final_score") private float finalScore;
    private AnalysedData analysed;

    @Getter @NoArgsConstructor
    public static class AnalysedData {
        private Section<CenterItem> center;
        private ScoreSection<TiltItem> tilt;
        private ScoreSection<SpaceItem> space;
        private ScoreSection<SizeItem> size;
        @JsonProperty("jamo_position") private ScoreSection<JamoDetailItem> jamoPosition;
        @JsonProperty("jamo_shape") private JamoShapeSection jamoShape;
    }

    @Getter @NoArgsConstructor
    public static class Section<T> {
        private List<T> items;
    }

    @Getter @NoArgsConstructor
    public static class ScoreSection<T> extends Section<T> {
        @JsonProperty("tilt_score") private float tiltScore;
        @JsonProperty("space_score") private float spaceScore;
        @JsonProperty("size_score") private float sizeScore;
        @JsonProperty("jamo_position_score") private float jamoPositionScore;
        @JsonProperty("jamo_shape_score") private float jamoShapeScore;
    }

    // --- 상세 항목 (chr 변수명 적용) ---
    @Getter @NoArgsConstructor
    public static class CenterItem {
        @JsonProperty("char") private String chr; // char 대신 chr 사용 [cite: 2025-12-24]
        private float x, y;
    }

    @Getter @NoArgsConstructor
    public static class TiltItem {
        private String char1, char2;
        private float tilt;
    }

    @Getter @NoArgsConstructor
    public static class SpaceItem {
        private String char1, char2;
        private float space;
        private boolean gap;
    }

    @Getter @NoArgsConstructor
    public static class SizeItem {
        @JsonProperty("char") private String chr;
        private float x1, y1, x2, y2, size;
    }

    @Getter @NoArgsConstructor
    public static class JamoDetailItem {
        @JsonProperty("char") private String chr;
        private JamoComponent chosung, jungsung, jongsung;
    }

    @Getter @NoArgsConstructor
    public static class JamoShapeSection {
        @JsonProperty("jamo_shape_score") private float jamoShapeScore;
        private List<JamoScoreItem> items;
    }

    @Getter @NoArgsConstructor
    public static class JamoScoreItem {
        @JsonProperty("char") private String chr;
        private ScoreSet chosung, jungsung, jongsung;
    }

    @Getter @NoArgsConstructor
    public static class JamoComponent {
        private BBox bbox;
        private float score;
    }

    @Getter @NoArgsConstructor
    public static class BBox {
        private float x1, y1, x2, y2;
    }

    @Getter @NoArgsConstructor
    public static class ScoreSet {
        private float score;
    }
}