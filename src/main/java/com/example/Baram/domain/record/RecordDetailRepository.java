package com.example.Baram.domain.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 필기 분석 상세 데이터(RecordDetail)에 대한 데이터 액세스를 담당하는 레포지토리 인터페이스입니다.
 * {@link Record}와 식별자(PK)를 공유하므로, Record ID를 통해 상세 분석 결과를 조회합니다.
 */
@Repository
public interface RecordDetailRepository extends JpaRepository<RecordDetail, Long> {
    // Record의 ID를 PK로 공유하므로, 기본 제공되는 findById(Long id)로 1:1 조회가 가능합니다. [cite: 2025-12-24]
}