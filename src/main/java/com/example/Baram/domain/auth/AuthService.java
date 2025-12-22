package com.example.Baram.domain.auth;

import com.example.Baram.domain.user.User;
import com.example.Baram.domain.user.UserRepository;
import com.example.Baram.domain.user.LoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public Session socialLogin(String socialId, LoginType loginType) {
        // 1. User DB에서 소셜 ID로 유저 조회, 없으면 생성(회원가입)
        User user = userRepository.findBySocialId(socialId)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .socialId(socialId)
                                .loginType(loginType)
                                .createdAt(LocalDateTime.now())
                                .build()
                ));

        // 2. 로그인 성공 시 새로운 Session 생성
        return createNewSession(user);
    }

    private Session createNewSession(User user) {
        // 기존 세션이 있다면 삭제하거나 유지하는 정책 선택 가능 (여기서는 새로 생성)
        String token = UUID.randomUUID().toString();

        Session session = Session.builder()
                .sessionToken(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(30)) // 30일 유효
                .build();

        return sessionRepository.save(session);
    }
}