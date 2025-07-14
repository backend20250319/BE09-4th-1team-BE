package com.playvoice.commentservice.command.repository;

import com.playvoice.commentservice.command.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    void deleteByCommentIdAndUserId(Long commentId, long userId);

    int countByCommentId(Long commentId);
}
