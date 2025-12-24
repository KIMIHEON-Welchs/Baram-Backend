package com.example.Baram.domain.record;

import com.example.Baram.domain.record.dto.RecordAnalysisResponse;
import com.example.Baram.domain.record.dto.RecordResponse;
import com.example.Baram.domain.record.dto.RecordSubmitRequest;
import com.example.Baram.domain.user.User;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 필기 분석 기록의 생성 및 조회를 처리하는 컨트롤러입니다.
 * 사용자의 필기 이미지를 접수하여 AI 분석을 트리거하고, 저장된 분석 결과를 반환합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
public class RecordController {

    /**
     * 사용자의 손글씨 이미지를 제출받아 AI 분석을 시작합니다.
     * 폰트 정보와 문장 텍스트(JSON) 및 이미지 파일(Multipart)을 함께 수신합니다. [cite: 2025-12-24, 2025-12-25]
     *
     * @param user    인증 필터에서 주입된 현재 로그인 사용자 [cite: 2025-12-24]
     * @param file    사용자가 직접 쓴 필기 이미지 파일
     * @param request 분석에 필요한 메타데이터 (폰트명, 문장 내용 등)
     * @return 생성된 기록 ID를 포함한 성공 메시지 [cite: 2025-12-25]
     */
    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> submitRecord(
            @AuthenticationPrincipal User user, // 인증 객체 직접 수신
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") RecordSubmitRequest request
    ) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 정보가 없습니다.");
        }

        try {
            // 서비스의 통합 제출 메서드 호출 [cite: 2025-12-24]
            Long recordId = recordService.submitRecord(user.getUserId(), request, file, file.getOriginalFilename());
            return ResponseEntity.ok("기록 제출 및 AI 분석 완료! ID: " + recordId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("분석 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 현재 로그인한 사용자의 모든 분석 이력을 최신순으로 조회합니다.
     *
     * @param user 현재 로그인 사용자
     * @return 요약된 분석 기록 리스트
     */
    @GetMapping
    public ResponseEntity<List<RecordResponse>> getMyRecords(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(recordService.getUserRecords(user.getUserId()));
    }

    /**
     * 특정 기록에 대한 정밀 분석 결과를 조회합니다.
     * PostgreSQL의 JSONB 데이터와 자모 분리 이미지 경로를 통합하여 반환합니다.
     *
     * @param recordId 조회할 기록의 고유 식별자
     * @return 상세 분석 데이터 및 세그먼트 이미지 경로
     */
    @GetMapping("/{recordId}/analysis")
    public ResponseEntity<RecordAnalysisResponse> getRecordAnalysis(@PathVariable Long recordId) {
        RecordAnalysisResponse analysis = recordService.getDetailedAnalysis(recordId);
        return ResponseEntity.ok(analysis);
    }

    private final RecordService recordService;
}