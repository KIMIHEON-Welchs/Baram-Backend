package com.example.Baram.domain.auth;

import com.example.Baram.domain.user.LoginType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Session> login(@RequestBody LoginRequest request) {
        Session session = authService.socialLogin(request.getSocialId(), request.getLoginType());
        return ResponseEntity.ok(session);
    }

    // 요청 DTO
    @Getter
    @NoArgsConstructor
    public static class LoginRequest {
        private String socialId;
        private LoginType loginType;
    }
}