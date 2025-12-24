package com.example.Baram.global.util;

import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

/**
 * 시스템 내 파일 저장 및 관리를 담당하는 유틸리티 클래스입니다.
 * 특히 AI 분석 결과로 생성된 Base64 이미지 데이터를 물리 파일로 변환하여 저장합니다.
 */
@Component
public class FileUtil {

    /**
     * Base64 문자열을 디코딩하여 이미지 파일로 저장하고, 접근 가능한 상대 경로를 반환합니다.
     * 저장 구조: uploads/segments/{recordId}/{subDir}/{UUID}.png
     *
     * @param recordId    분석 기록의 고유 식별자 (폴더 구분용)
     * @param subDir      세부 카테고리 (whole, chosung, jungsung, jongsung 등)
     * @param base64Image 변환할 Base64 인코딩 이미지 문자열
     * @return 저장된 파일의 상대 경로 (DB 저장용)
     * @throws IOException 파일 쓰기 실패 시 발생
     */
    public String saveBase64Image(Long recordId, String subDir, String base64Image) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }

        // 저장 디렉토리 생성 (예: uploads/segments/1/chosung/)
        // 파일이 저장될 기본 루트 경로 (환경에 맞춰 수정 가능)
        String UPLOAD_DIR = "uploads/segments/";
        String fullPath = UPLOAD_DIR + recordId + "/" + subDir + "/";
        File directory = new File(fullPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 파일명 생성 (중복 방지를 위해 UUID 사용)
        String fileName = UUID.randomUUID().toString() + ".png";
        File file = new File(directory, fileName);

        // Base64 디코딩 및 파일 쓰기
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageBytes);
        }

        // DB에 저장할 상대 경로 반환
        return fullPath + fileName;
    }
}