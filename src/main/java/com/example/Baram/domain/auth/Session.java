package com.example.Baram.domain.auth;

import com.example.Baram.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 사용자 세션 정보를 저장하는 엔티티입니다.
 * 토큰을 기반으로 사용자 엔티티와 다대일(N:1) 관계를 맺습니다.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session
{

    @Id
    @Column(length = 255) // 토큰 자체가 PK 역할
    private String sessionToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt; // 토큰 만료 시간
}