package com.example.Baram.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자(User) 엔티티에 대한 데이터 액세스를 관리하는 레포지토리입니다.
 * 소셜 식별자 기반 조회 및 기본적인 회원 정보 관리 기능을 제공합니다.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findBySocialId(String socialId);
    Optional<User> findByUserId(long userId);
}