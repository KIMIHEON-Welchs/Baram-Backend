package com.example.Baram.domain.record.dto;

import lombok.*;
import java.util.List;

/**
 * 필기 분석의 통합 상세 결과를 담는 데이터 전송 객체(DTO)입니다.
 * 항목별 점수, 세부 좌표 JSON, 그리고 자모 분리 이미지 경로를 모두 포함합니다.
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordAnalysisResponse {

    // 1. 5가지 항목별 상세 점수 (RecordDetail에서 추출)
    private Float tiltScore;
    private Float spaceScore;
    private Float sizeScore;
    private Float jamoPositionScore;
    private Float jamoShapeScore;

    // 2. 전체 분석 좌표 및 세부 수치 JSON (RecordDetail의 detailedJson)
    private Object detailedJson; // JSON 형태 그대로 내려주기 위해 Object 타입 사용

    // 3. 글자별 자모 이미지 경로 리스트 (RecordSegment에서 추출)
    private List<SegmentResponse> segments;

    /**
     * 개별 글자의 이미지 경로를 담는 내부 DTO
     */
    @Getter @Setter
    @Builder
    public static class SegmentResponse {
        private int charIndex;      // 문장 내 위치
        private String character;   // 글자 (예: '안')
        private String wholePath;   // 전체 글자 이미지 URL
        private String chosungPath; // 초성 이미지 URL
        private String jungsungPath;// 중성 이미지 URL
        private String jongsungPath;// 종성 이미지 URL (없으면 null)
    }
}