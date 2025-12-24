package com.example.Baram.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 서비스 이용자 정보를 관리하는 엔티티입니다.
 * 소셜 로그인 정보와 앱 내에서 사용할 프로필 데이터를 포함합니다.
 */
@Entity
@Getter @Setter
@NoArgsConstructor // 기본 생성자 자동 생성 (JPA 필수)
@AllArgsConstructor // 모든 필드 생성자
@Builder // 빌더 패턴 사용 가능 (객체 생성 시 편리)
@Table(name = "users") // 주의: PostgreSQL에서 'user'는 예약어일 수 있어 'users'로 저장하는 게 안전함
public class User
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    @Column(name = "user_id")
    private Long userId;

    /**
     * 앱 내에서 표시될 사용자 닉네임입니다.
     */
    @Column(length = 50)
    private String nickname;

    /**
     * 사용자의 프로필 이미지 경로 또는 URL입니다.
     */
    @Column(columnDefinition = "TEXT")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType loginType;

    @Column(length = 255)
    private String socialId; // 소셜 로그인 고유 ID 값

    @Column(columnDefinition = "TEXT") // PostgreSQL의 TEXT 타입 매핑
    private String onboardingData;

    @CreationTimestamp // INSERT 시 자동으로 현재 시간 저장
    @Column(nullable = false, updatable = false) // 수정 불가
    private LocalDateTime createdAt;

    /**
     * 사용자의 프로필 정보(닉네임, 이미지)를 업데이트합니다.
     * @param nickname 새로운 닉네임
     * @param profileImageUrl 새로운 프로필 이미지 주소
     */
    public void updateProfile(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}