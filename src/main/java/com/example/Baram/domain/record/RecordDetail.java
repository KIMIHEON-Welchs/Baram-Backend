package com.example.Baram.domain.record;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordDetail {

    @Id
    @Column(name = "record_id")
    private Long recordId; // Record의 PK를 공유 [cite: 2025-12-24]

    // --- 관계 매핑 (1:1 공유 PK 방식) ---
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Record의 ID를 이 엔티티의 ID로 사용 [cite: 2025-12-24]
    @JoinColumn(name = "record_id")
    private Record record;

    // --- 상세 분석 점수 (상세 수치) ---
    private Float tiltScore;         // 기울기 점수 [cite: 2025-12-24]
    private Float spaceScore;        // 자간 점수 [cite: 2025-12-24]
    private Float sizeScore;         // 크기 점수 [cite: 2025-12-24]
    private Float jamoPositionScore; // 자모 위치 점수 [cite: 2025-12-24]
    private Float jamoShapeScore;    // 자모 형태 점수 [cite: 2025-12-24]

    // --- 전체 분석 데이터 (JSONB) ---
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String detailedJson; // analysed.json의 전체 계층 구조 저장 [cite: 2025-12-24]
}