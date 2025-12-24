package com.example.Baram.global.util;

import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Component
public class FileUtil {

    /**
     * Base64 문자열을 이미지 파일로 저장하고 저장된 상대 경로를 반환합니다. [cite: 2025-12-18, 2025-12-24]
     */
    public String saveBase64Image(Long recordId, String subDir, String base64Image) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }

        // 1. 저장 디렉토리 생성 (예: uploads/segments/1/chosung/) [cite: 2025-12-24]
        // 파일이 저장될 기본 루트 경로 (환경에 맞춰 수정 가능) [cite: 2025-12-24]
        String UPLOAD_DIR = "uploads/segments/";
        String fullPath = UPLOAD_DIR + recordId + "/" + subDir + "/";
        File directory = new File(fullPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 2. 파일명 생성 (중복 방지를 위해 UUID 사용) [cite: 2025-12-24]
        String fileName = UUID.randomUUID().toString() + ".png";
        File file = new File(directory, fileName);

        // 3. Base64 디코딩 및 파일 쓰기 [cite: 2025-12-18, 2025-12-24]
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageBytes);
        }

        // 4. DB에 저장할 상대 경로 반환 [cite: 2025-12-24]
        return fullPath + fileName;
    }
}