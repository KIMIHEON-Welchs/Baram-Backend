package com.example.Baram.domain.auth;

import com.example.Baram.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime expiresAt; // 토큰 만료 시간
}