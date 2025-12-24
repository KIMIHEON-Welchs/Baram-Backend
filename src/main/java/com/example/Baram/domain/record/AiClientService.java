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

@Service
@RequiredArgsConstructor
public class AiClientService {

    private final RestTemplate restTemplate;
    // AI 서버 주소 (추후 변경 가능하도록 변수화) [cite: 2025-12-24]
    private final String AI_SERVER_URL = "http://localhost:5000";

    /**
     * 1. 분석 결과 수신 (analysed.json 대응) [cite: 2025-12-24]
     */
    public AiAnalysisResponse sendToAnalysis(MultipartFile file, String fontName) {
        String url = AI_SERVER_URL + "/analyze";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource()); // 파일을 Resource로 전달 [cite: 2025-12-24]
        body.add("font", fontName);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(url, requestEntity, AiAnalysisResponse.class);
    }

    /**
     * 2. 자모 분리 이미지 수신 (char.json 대응) [cite: 2025-12-24]
     */
    public AiCharResponse sendToSegmentation(MultipartFile file) {
        String url = AI_SERVER_URL + "/segment";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(url, requestEntity, AiCharResponse.class);
    }
}