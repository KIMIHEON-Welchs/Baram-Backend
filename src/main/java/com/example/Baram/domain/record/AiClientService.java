package com.example.Baram.domain.record;

import com.example.Baram.domain.record.dto.AiAnalysisResponse;
import com.example.Baram.domain.record.dto.AiCharResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * AI 분석 서버와의 통신을 전담하는 클라이언트 서비스입니다.
 * 필기 이미지 분석 및 자모 세그멘테이션 요청을 처리하고 결과를 수신합니다.
 */
@Service
@RequiredArgsConstructor
public class AiClientService {

    private final RestTemplate restTemplate;
    /**
     * AI 서버 엔드포인트 주소입니다.
     * 현재 로컬 주소로 설정되어 있으며, 운영 환경에 따라 외부 설정 파일로 관리할 것을 권장합니다.
     */
    private final String AI_SERVER_URL = "http://localhost:5000";

    /**
     * 필기 이미지에 대한 정밀 분석(기울기, 간격, 크기 등)을 요청합니다.
     *
     * @param file     사용자가 작성한 필기 이미지 파일
     * @param fontName 비교 기준이 될 폰트 명칭 (예: 나눔바른펜)
     * @return AI 서버에서 산출된 정밀 분석 데이터와 점수가 담긴 DTO
     */
    public AiAnalysisResponse sendToAnalysis(MultipartFile file, String fontName) {
        String url = AI_SERVER_URL + "/analyze";

        // 멀티파트 요청을 위한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 이미지 파일 리소스와 폰트 데이터를 바디에 담음
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource()); // 파일을 Resource로 전달
        body.add("font", fontName);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // AI 서버로부터 분석 결과 수신 및 DTO 매핑
        return restTemplate.postForObject(url, requestEntity, AiAnalysisResponse.class);
    }

    /**
     * 필기 이미지에서 각 글자 및 자모(초/중/종성)를 분리하는 작업을 요청합니다.
     *
     * @param file 분리 분석을 수행할 필기 이미지 파일
     * @return 각 글자별 인덱스와 Base64 인코딩된 자모 이미지 정보가 담긴 DTO
     */
    public AiCharResponse sendToSegmentation(MultipartFile file) {
        String url = AI_SERVER_URL + "/segment";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // AI 서버로부터 자모 분리 결과 수신
        return restTemplate.postForObject(url, requestEntity, AiCharResponse.class);
    }
}