package com.playvoice.postservice.answer.repository;

import com.playvoice.postservice.answer.entity.AnswerPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnswerPostRepository extends JpaRepository<AnswerPostEntity, Long> {

}
