package com.example.Baram.domain.auth;

import com.example.Baram.domain.user.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 * 모든 요청마다 세션 토큰의 유효성을 검사하는 필터입니다.
 * 유효한 토큰일 경우 Spring Security Context에 인증 정보를 등록합니다.
 */
@Component
@RequiredArgsConstructor
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Header에서 Authorization: Bearer <token> 추출
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // 2. DB에서 세션 조회 및 만료 여부 확인
            sessionRepository.findById(token).ifPresent(session -> {
                if (session.getExpiresAt().isAfter(LocalDateTime.now())) {

                    User user = session.getUser();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            });
        }

        filterChain.doFilter(request, response);
    }

    private final SessionRepository sessionRepository;
}