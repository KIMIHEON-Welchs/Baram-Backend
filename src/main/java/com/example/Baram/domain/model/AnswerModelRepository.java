// 예시: SentenceLibraryRepository.java
package com.example.Baram.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerModelRepository extends JpaRepository<AnswerModel, Long>
{
    Optional<AnswerModel> findByModelName(String modelName);
}