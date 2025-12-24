package com.example.Baram.domain.system;

import com.example.Baram.domain.system.dto.UserSettingsResponse;
import com.example.Baram.domain.user.User;
import com.example.Baram.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SystemService {

    private final UserRepository userRepository;

    /**
     * 현재 로그인 사용자의 최신 프로필 정보를 조회하여 반환합니다.
     */
    @Transactional(readOnly = true)
    public UserSettingsResponse getUserSettings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return UserSettingsResponse.from(user);
    }

    /**
     * 사용자의 프로필(닉네임, 사진)을 수정합니다.
     * @param userId 수정할 사용자 ID
     * @param nickname 변경할 닉네임
     * @param profileImage 변경할 프로필 이미지 파일 (선택사항)
     */
    @Transactional
    public UserSettingsResponse updateProfile(Long userId, String nickname, MultipartFile profileImage) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String finalProfileUrl = user.getProfileImageUrl();

        // 새로운 이미지가 업로드된 경우 파일 저장 (FileUtil 활용)
        if (profileImage != null && !profileImage.isEmpty()) {
            // 프로필 전용 디렉토리에 저장하도록 경로 설정
            String fileName = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();
            String uploadPath = "uploads/profiles/" + userId + "/";

            File directory = new File(uploadPath);
            if (!directory.exists()) directory.mkdirs();

            File targetFile = new File(directory, fileName);
            profileImage.transferTo(targetFile);

            finalProfileUrl = "/" + uploadPath + fileName;
        }

        // 2. 엔티티 내부의 updateProfile 메서드를 통한 상태 변경 (캡슐화)
        user.updateProfile(nickname, finalProfileUrl);

        // 3. 변경된 정보를 DTO로 변환하여 반환
        return UserSettingsResponse.from(user);
    }
}