package com.playvoice.postservice.repository.jpa;

import com.playvoice.postservice.entity.AnswerPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnswerPostRepository extends JpaRepository<AnswerPostEntity, Long> {

}
