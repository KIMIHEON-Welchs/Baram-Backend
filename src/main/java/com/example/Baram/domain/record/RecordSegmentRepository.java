package com.example.Baram.domain.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 필기 데이터의 개별 글자 및 자모 분리 이미지 경로(RecordSegment)를 관리하는 레포지토리입니다.
 * 문장을 구성하는 각 글자별 이미지 데이터를 DB에서 추출하는 기능을 제공합니다.
 */
@Repository
public interface RecordSegmentRepository extends JpaRepository<RecordSegment, Long> {
    // 특정 기록에 속한 모든 글자 이미지 경로를 순서대로 조회 [cite: 2025-12-24]
    List<RecordSegment> findByRecordOrderByCharIndexAsc(Record record);
}