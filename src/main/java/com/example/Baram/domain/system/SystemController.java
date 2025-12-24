package com.example.Baram.domain.system;

import com.example.Baram.domain.system.dto.UserSettingsResponse;
import com.example.Baram.domain.user.User;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 앱 시스템 설정 및 사용자 환경설정을 관리하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me")
public class SystemController {

    private final SystemService systemService;

    /**
     * 설정 화면 진입 시 필요한 사용자 프로필 데이터를 반환합니다.
     * 민감한 socialId 대신 닉네임과 프로필 정보를 제공합니다.
     */
    @GetMapping("/settings")
    public ResponseEntity<UserSettingsResponse> getMySettings(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        UserSettingsResponse response = systemService.getUserSettings(user.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자의 닉네임과 프로필 사진을 변경합니다.
     */
    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserSettingsResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestPart("nickname") String nickname,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) throws IOException {
        if (user == null) return ResponseEntity.status(401).build();

        UserSettingsResponse updated = systemService.updateProfile(user.getUserId(), nickname, profileImage);
        return ResponseEntity.ok(updated);
    }
}