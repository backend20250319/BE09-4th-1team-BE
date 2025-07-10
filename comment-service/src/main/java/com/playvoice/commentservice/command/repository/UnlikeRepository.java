package com.playvoice.commentservice.command.repository;

import com.playvoice.commentservice.command.entity.Unlike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnlikeRepository extends JpaRepository<Unlike, Long> {

    void deleteByCommentIdAndUserId(Long commentId, long userId);

    int countByCommentId(Long commentId);
}
