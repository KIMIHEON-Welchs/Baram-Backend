// 예시: SentenceLibraryRepository.java
package com.example.Baram.domain.sentence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SentenceLibraryRepository extends JpaRepository<SentenceLibrary, Long>
{
    // 활성화된 모든 문장 조회
    List<SentenceLibrary> findAllByIsActiveTrue();

    // 카테고리별 활성화된 문장 조회
    List<SentenceLibrary> findByCategoryAndIsActiveTrue(String category);
}