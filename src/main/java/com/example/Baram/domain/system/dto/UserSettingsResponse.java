package com.example.Baram.domain.system.dto;

import com.example.Baram.domain.user.LoginType;
import com.example.Baram.domain.user.User;
import lombok.Builder;
import lombok.Getter;

/**
 * 앱 설정창(Settings)에서 사용자 프로필 정보 표시를 위한 DTO입니다.
 */
@Getter
@Builder
public class UserSettingsResponse {
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private LoginType loginType;

    /**
     * User 엔티티를 설정창 응답 규격으로 변환합니다.
     */
    public static UserSettingsResponse from(User user) {
        return UserSettingsResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .loginType(user.getLoginType())
                .build();
    }
}