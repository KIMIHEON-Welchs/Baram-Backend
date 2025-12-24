package com.example.Baram.domain.record;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 필기 분석 기록에 대한 검색 엔진(Elasticsearch) 전용 레포지토리입니다.
 * 문장 내용이나 피드백 텍스트를 이용한 고속 전문 검색 기능을 수행합니다.
 */
@Repository
public interface RecordSearchRepository extends ElasticsearchRepository<RecordDocument, String>
{
    // 1. 유저의 기록 중 특정 단어가 들어간 것 찾기
    List<RecordDocument> findByUserIdAndSentenceContentContaining(Long userId, String keyword);

    // 2. 피드백 내용으로 검색 ("자음" 관련 피드백 받은거 보여줘)
    List<RecordDocument> findByFeedbackTipContaining(String keyword);
}