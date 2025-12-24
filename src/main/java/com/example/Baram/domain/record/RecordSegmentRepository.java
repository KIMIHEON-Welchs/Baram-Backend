package com.example.Baram.domain.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecordSegmentRepository extends JpaRepository<RecordSegment, Long> {
    // 특정 기록에 속한 모든 글자 이미지 경로를 순서대로 조회 [cite: 2025-12-24]
    List<RecordSegment> findByRecordOrderByCharIndexAsc(Record record);
}