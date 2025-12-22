package com.example.Baram.domain.record.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class AiAnalysisResponse
{
    // Python 서버가 줄 JSON 형식을 미리 정의함
    private Float score;              // 점수
    private String recognizedText;    // 인식한 텍스트

    // 상세 피드백 내용
    private String jamoShapeComment;  // 자음 모양 피드백
    private String spacingComment;    // 띄어쓰기 피드백
    private String alignmentComment;  // 줄 맞춤 피드백
    private String totalTip;          // 총평
}