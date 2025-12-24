package com.example.Baram.domain.auth;

import com.example.Baram.domain.user.LoginType;
import com.example.Baram.domain.user.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 * 소셜 로그인 및 세션 발급을 담당합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<Session> login(@RequestBody LoginRequest request) {
        Session session = authService.socialLogin(request.getSocialId(), request.getLoginType());
        return ResponseEntity.ok(session);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal User user) {
        if (user != null) {
            authService.logout(user);
        }
        return ResponseEntity.ok("Successfully logged out");
    }

    // 요청 DTO
    @Getter
    @NoArgsConstructor
    public static class LoginRequest {
        private String socialId;
        private LoginType loginType;
    }

    private final AuthService authService;
}