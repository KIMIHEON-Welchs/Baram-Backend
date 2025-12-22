package com.example.Baram.domain.sentence;

import com.example.Baram.domain.sentence.dto.SentenceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sentences")
public class SentenceController {

    private final SentenceService sentenceService;

    // GET /api/sentences?category=명언
    @GetMapping
    public ResponseEntity<List<SentenceResponse>> getSentences(
            @RequestParam(required = false) String category
    ) {
        return ResponseEntity.ok(sentenceService.getSentences(category));
    }
}