package com.example.Baram.domain.sentence;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SentenceSearchRepository extends ElasticsearchRepository<SentenceDocument, String>
{
    // 1. 내용(content)에 키워드가 포함된 문장 찾기
    List<SentenceDocument> findByContentContaining(String keyword);

    // 2. 카테고리(category)가 정확히 일치하는 문장 찾기
    List<SentenceDocument> findByCategory(String category);
}