package com.example.Baram.domain.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordDetailRepository extends JpaRepository<RecordDetail, Long> {
    // Record의 ID를 PK로 공유하므로, 기본 제공되는 findById(Long id)로 1:1 조회가 가능합니다. [cite: 2025-12-24]
}