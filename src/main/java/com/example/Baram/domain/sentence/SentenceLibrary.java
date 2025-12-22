package com.example.Baram.domain.sentence;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentenceLibrary
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sentence_id")
    private Long sentenceId;

    @Column(columnDefinition = "TEXT", nullable = false) // 문장 내용은 길 수 있으니 TEXT
    private String content;

    @Column(length = 50)
    private String category; // 예: "속담", "명언", "시"

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    private SentenceLevel level;

    @Column(nullable = false)
    private Boolean isActive; // 이 문장을 사용자에게 보여줄지 말지 (삭제 대신 숨김 처리용)

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}