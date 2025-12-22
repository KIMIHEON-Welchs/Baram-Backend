package com.example.Baram.domain.sentence;

import com.example.Baram.domain.sentence.dto.SentenceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SentenceService {

    private final SentenceLibraryRepository sentenceRepository;

    @Transactional(readOnly = true)
    public List<SentenceResponse> getSentences(String category) {
        List<SentenceLibrary> sentences;

        if (category != null && !category.isEmpty()) {
            sentences = sentenceRepository.findByCategoryAndIsActiveTrue(category);
        } else {
            sentences = sentenceRepository.findAllByIsActiveTrue();
        }

        return sentences.stream()
                .map(SentenceResponse::from)
                .collect(Collectors.toList());
    }
}