package com.example.Baram.domain.record;

public enum AnalysisStatus
{
    PENDING,    // 분석 대기 중
    PROCESSING, // 분석 중 (AI 도는 중)
    COMPLETED,  // 분석 완료 (점수 나옴)
    FAILED      // 분석 실패 (이미지 깨짐 등)
}