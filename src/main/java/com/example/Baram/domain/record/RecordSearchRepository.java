package com.example.Baram.domain.record;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RecordSearchRepository extends ElasticsearchRepository<RecordDocument, String>
{
    // 1. 유저의 기록 중 특정 단어가 들어간 것 찾기
    List<RecordDocument> findByUserIdAndSentenceContentContaining(Long userId, String keyword);

    // 2. 피드백 내용으로 검색 ("자음" 관련 피드백 받은거 보여줘)
    List<RecordDocument> findByFeedbackTipContaining(String keyword);
}