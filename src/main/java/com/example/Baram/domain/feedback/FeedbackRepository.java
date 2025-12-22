package com.example.Baram.domain.feedback;

import com.example.Baram.domain.record.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>
{
    Optional<Feedback> findByRecord(Record record);
}