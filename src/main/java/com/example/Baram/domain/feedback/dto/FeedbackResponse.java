package com.example.Baram.domain.feedback.dto;

import com.example.Baram.domain.feedback.Feedback;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackResponse {
    private String jamoShape;
    private String jamoSpacing;
    private String letterSpacing;
    private String alignment;
    private String improvementTip;

    public static FeedbackResponse from(Feedback feedback) {
        return FeedbackResponse.builder()
                .jamoShape(feedback.getAnalysis1JamoShape())
                .jamoSpacing(feedback.getAnalysis2JamoSpacing())
                .letterSpacing(feedback.getAnalysis3LetterSpacing())
                .alignment(feedback.getAnalysis4SentenceAlignment())
                .improvementTip(feedback.getImprovementTip())
                .build();
    }
}