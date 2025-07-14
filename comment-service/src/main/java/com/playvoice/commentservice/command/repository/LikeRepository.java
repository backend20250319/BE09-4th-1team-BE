package com.playvoice.commentservice.command.repository;

import com.playvoice.commentservice.command.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Like l WHERE l.comment.id = :commentId AND l.userId = :userId")
    int deleteByCommentIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);

    int countByCommentId(Long commentId);
}