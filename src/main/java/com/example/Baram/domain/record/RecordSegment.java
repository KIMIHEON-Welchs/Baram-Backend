package com.example.Baram.domain.record;

import jakarta.persistence.*;
import lombok.*;

/**
 * 분석된 문장의 개별 글자 및 자모 분리 이미지 정보를 저장하는 엔티티입니다.
 * 문장 내 글자 순서와 각 자모(초/중/종성)별 이미지 파일 경로를 관리합니다.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long segmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    private int charIndex; // 문장 내 글자 순서 (0부터 시작) [cite: 2025-12-24]
    private String chr;       // 분석된 해당 글자 (예: '안') [cite: 2025-12-24]

    // 서버 내 이미지 파일 저장 경로들 [cite: 2025-12-24]
    private String wholePath;
    private String chosungPath;
    private String jungsungPath;
    private String jongsungPath;

    @Builder
    public RecordSegment(Record record, int charIndex, String chr, String wholePath,
                         String chosungPath, String jungsungPath, String jongsungPath) {
        this.record = record;
        this.charIndex = charIndex;
        this.chr = chr;
        this.wholePath = wholePath;
        this.chosungPath = chosungPath;
        this.jungsungPath = jungsungPath;
        this.jongsungPath = jongsungPath;
    }
}