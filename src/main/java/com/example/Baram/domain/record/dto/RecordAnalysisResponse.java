package com.example.Baram.domain.record.dto;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordAnalysisResponse {

    // 1. 5가지 항목별 상세 점수 (RecordDetail에서 추출) [cite: 2025-12-24]
    private Float tiltScore;
    private Float spaceScore;
    private Float sizeScore;
    private Float jamoPositionScore;
    private Float jamoShapeScore;

    // 2. 전체 분석 좌표 및 세부 수치 JSON (RecordDetail의 detailedJson) [cite: 2025-12-24]
    private Object detailedJson; // JSON 형태 그대로 내려주기 위해 Object 타입 사용 [cite: 2025-12-24]

    // 3. 글자별 자모 이미지 경로 리스트 (RecordSegment에서 추출) [cite: 2025-12-24]
    private List<SegmentResponse> segments;

    /**
     * 개별 글자의 이미지 경로를 담는 내부 DTO [cite: 2025-12-24]
     */
    @Getter @Setter
    @Builder
    public static class SegmentResponse {
        private int charIndex;      // 문장 내 위치 [cite: 2025-12-24]
        private String character;   // 글자 (예: '안') [cite: 2025-12-24]
        private String wholePath;   // 전체 글자 이미지 URL [cite: 2025-12-24]
        private String chosungPath; // 초성 이미지 URL [cite: 2025-12-24]
        private String jungsungPath;// 중성 이미지 URL [cite: 2025-12-24]
        private String jongsungPath;// 종성 이미지 URL (없으면 null) [cite: 2025-12-24]
    }
}