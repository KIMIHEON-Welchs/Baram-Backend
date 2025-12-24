package com.example.Baram.domain.record.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * AI 서버의 정밀 필기 분석 결과를 매핑하기 위한 통합 DTO입니다.
 * 5가지 핵심 지표(기울기, 간격, 크기, 위치, 모양)와 각 항목별 상세 좌표 및 점수를 포함합니다.
 */
@Getter @NoArgsConstructor
@SuppressWarnings("unused")
public class AiAnalysisResponse {
    private String sentence;
    private String font;
    @JsonProperty("final_score") private float finalScore;
    private AnalysedData analysed;

    /**
     * 각 분석 항목별 섹션을 포함하는 내부 클래스입니다.
     */
    @Getter @NoArgsConstructor
    public static class AnalysedData {
        private Section<CenterItem> center;
        private ScoreSection<TiltItem> tilt;
        private ScoreSection<SpaceItem> space;
        private ScoreSection<SizeItem> size;
        @JsonProperty("jamo_position") private ScoreSection<JamoDetailItem> jamoPosition;
        @JsonProperty("jamo_shape") private JamoShapeSection jamoShape;
    }

    /**
     * 기본 리스트 형태의 데이터를 담는 제네릭 섹션입니다.
     */
    @Getter @NoArgsConstructor
    public static class Section<T> {
        private List<T> items;
    }

    /**
     * 리스트 데이터와 해당 항목의 대표 점수를 함께 담는 섹션입니다.
     */
    @Getter @NoArgsConstructor
    public static class ScoreSection<T> extends Section<T> {
        @JsonProperty("tilt_score") private float tiltScore;
        @JsonProperty("space_score") private float spaceScore;
        @JsonProperty("size_score") private float sizeScore;
        @JsonProperty("jamo_position_score") private float jamoPositionScore;
        @JsonProperty("jamo_shape_score") private float jamoShapeScore;
    }

    // --- 상세 항목 ---
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