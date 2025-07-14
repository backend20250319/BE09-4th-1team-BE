package com.playvoice.postservice.answer.repository;

import com.playvoice.postservice.answer.entity.AnswerPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaAnswerPostRepository extends JpaRepository<AnswerPostEntity, Long> {
    Optional<AnswerPostEntity> findBySuggestionPostId(Long suggestionPostId);
}
