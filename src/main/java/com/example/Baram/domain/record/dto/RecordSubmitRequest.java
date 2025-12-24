package com.example.Baram.domain.record.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 필기 분석 요청 시 전달받는 데이터 전송 객체(DTO)입니다.
 * 사용자가 선택한 폰트와 작성한 문장 정보를 담습니다.
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordSubmitRequest
{
    private String font;
    private String sentence;
}