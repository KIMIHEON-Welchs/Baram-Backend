package com.example.Baram.domain.record;

import com.example.Baram.domain.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 필기 분석 기록(Record) 엔티티에 대한 데이터 액세스를 담당하는 레포지토리 인터페이스입니다.
 * JpaRepository를 상속받아 기본적인 CRUD 및 페이징 기능을 제공받습니다.
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, Long>
{
    // 필요한 경우 여기에 검색 메소드 추가 (예: 특정 유저의 기록 조회)
    List<Record> findByUserOrderBySubmissionDateDesc(User user);
}