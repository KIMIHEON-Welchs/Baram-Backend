package com.example.Baram.domain.record;

import com.example.Baram.domain.auth.Session;
import com.example.Baram.domain.auth.SessionRepository;
import com.example.Baram.domain.feedback.dto.FeedbackResponse;
import com.example.Baram.domain.record.dto.RecordAnalysisResponse;
import com.example.Baram.domain.record.dto.RecordResponse;
import com.example.Baram.domain.record.dto.RecordSubmitRequest;
import com.example.Baram.domain.user.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    // 1. 손글씨 기록 제출 및 AI 분석 시작
    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitRecord(
            @AuthenticationPrincipal User user, // 인증 객체 직접 수신 [cite: 2025-12-24]
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

    // 2. 내 전체 기록 조회 (시큐리티 적용 버전) [cite: 2025-12-24]
    @GetMapping
    public ResponseEntity<List<RecordResponse>> getMyRecords(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(recordService.getUserRecords(user.getUserId()));
    }

    // 3. [변경 필요] 특정 기록의 상세 분석 결과 조회
    @GetMapping("/{recordId}/analysis")
    public ResponseEntity<RecordAnalysisResponse> getRecordAnalysis(@PathVariable Long recordId) {
        // 기존 FeedbackResponse 대신, RecordDetail과 RecordSegment를 합친 새로운 DTO 반환 필요 [cite: 2025-12-24]
        RecordAnalysisResponse analysis = recordService.getDetailedAnalysis(recordId);
        return ResponseEntity.ok(analysis);
    }
}