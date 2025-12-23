package com.example.Baram.domain.record;

import com.example.Baram.domain.auth.Session;
import com.example.Baram.domain.auth.SessionRepository;
import com.example.Baram.domain.feedback.dto.FeedbackResponse;
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
public class RecordController
{

    // 1. 손글씨 기록 제출
    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitRecord(
            @AuthenticationPrincipal User user,
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") RecordSubmitRequest request
    ) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 정보가 없습니다.");
        }
        Long userId = user.getUserId();

        // 인자 순서: userId, DTO, 파일명 [cite: 2025-12-23]
        Long recordId = recordService.submitRecord(userId, request, file.getOriginalFilename());
        return ResponseEntity.ok("기록 저장 성공! ID: " + recordId);
    }

    // 2. 내 전체 기록 조회 (요약 리스트)
    @GetMapping
    public ResponseEntity<?> getMyRecords(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);

        Long userId = sessionRepository.findBySessionToken(token)
                .map(Session::getUserId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 세션토큰입니다."));

        List<RecordResponse> records = recordService.getUserRecords(userId);
        return ResponseEntity.ok(records);
    }

    // 2. 특정 기록의 상세 피드백 조회
// GET: localhost:8080/api/records/{recordId}/feedback
    @GetMapping("/{recordId}/feedback")
    public ResponseEntity<FeedbackResponse> getRecordFeedback(@PathVariable Long recordId) {
        FeedbackResponse feedback = recordService.getFeedbackByRecordId(recordId);
        return ResponseEntity.ok(feedback);
    }


    private final RecordService recordService;
    private final SessionRepository sessionRepository;
}