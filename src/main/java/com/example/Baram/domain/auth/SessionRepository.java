package com.example.Baram.domain.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {
    // 토큰으로 세션 존재 여부 및 유효성 확인 시 사용
    Optional<Session> findBySessionToken(String sessionToken);

    // 특정 유저의 기존 세션 삭제 시 사용 (중복 로그인 방지 등)
    void deleteByUserId(Long userId);
}