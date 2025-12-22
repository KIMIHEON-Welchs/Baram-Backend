package com.example.Baram.domain.record;

import com.example.Baram.domain.feedback.dto.FeedbackResponse;
import com.example.Baram.domain.record.dto.RecordResponse;
import com.example.Baram.domain.record.dto.RecordSubmitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
public class RecordController
{

    // POST: localhost:8080/api/records/submit
    // Content-Type: multipart/form-data
    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> submitRecord(
            @RequestPart("file") MultipartFile file,      // 이미지 파일
            @RequestPart("data") RecordSubmitRequest request // JSON 데이터
    ) {
        try {
            Long recordId = recordService.submitRecord(file, request);
            return ResponseEntity.ok("기록 저장 성공! ID: " + recordId);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("파일 저장 실패: " + e.getMessage());
        }
    }
    // domain.record.RecordController.java

    // 1. 내 전체 기록 조회 (요약 리스트)
// GET: localhost:8080/api/records?userId=1
    @GetMapping
    public ResponseEntity<List<RecordResponse>> getMyRecords(@RequestParam Long userId) {
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
}