package com.example.Baram.domain.record;

import com.example.Baram.domain.user.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long>
{
    // 필요한 경우 여기에 검색 메소드 추가 (예: 특정 유저의 기록 조회)
    List<Record> findByUserOrderBySubmissionDateDesc(User user);
}