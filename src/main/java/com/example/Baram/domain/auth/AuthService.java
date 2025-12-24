package com.example.Baram.domain.auth;

import com.example.Baram.domain.user.User;
import com.example.Baram.domain.user.UserRepository;
import com.example.Baram.domain.user.LoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 사용자 인증 및 세션 관리를 담당하는 서비스 클래스입니다.
 * 소셜 로그인 처리와 DB 기반의 세션 생성/삭제를 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    @Transactional
    public Session socialLogin(String socialId, LoginType loginType) {
        // User DB에서 소셜 ID로 유저 조회, 없으면 생성(회원가입)
        User user = userRepository.findBySocialId(socialId)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .socialId(socialId)
                                .loginType(loginType)
                                .createdAt(LocalDateTime.now())
                                .build()
                ));

        // 로그인 성공 시 새로운 Session 생성
        return createNewSession(user);
    }

    @Transactional
    public void logout(User user) {
        sessionRepository.deleteByUser(user);
    }

    private Session createNewSession(User user) {
        String token = UUID.randomUUID().toString();

        Session session = Session.builder()
                .sessionToken(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(30)) // 30일 유효
                .build();

        return sessionRepository.save(session);
    }

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
}