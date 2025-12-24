package com.example.Baram.domain.auth;

import com.example.Baram.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * 세션 엔티티에 대한 데이터 액세스 처리를 담당하는 레포지토리입니다.
 * sessionToken을 기본키(PK)로 사용합니다.
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    // findById()로 session조회

    // 특정 유저의 기존 세션 삭제 시 사용 (중복 로그인 방지 등)
    void deleteByUserId(Long userId); // userId로 session삭제
    void deleteByUser(User user); // user로 session삭제
}