package com.example.Baram.global.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * 외부 API(특히 AI 분석 서버)와의 통신을 위한 RestTemplate 설정을 담당하는 클래스입니다.
 * 네트워크 연결 및 데이터 응답 대기 시간에 대한 정책을 정의합니다.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * AI 분석 서버의 처리 시간을 고려하여 타임아웃이 설정된 RestTemplate 빈을 생성합니다.
     * * @param builder 스프링 부트에서 제공하는 RestTemplate 빌더
     * @return 타임아웃 정책이 적용된 RestTemplate 객체
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.connectTimeout(Duration.ofSeconds(5)).readTimeout(Duration.ofSeconds(30))    // 데이터 읽기 대기 시간 30초 (AI 분석 시간 고려) [cite: 2025-12-24]
                .build();
    }
}