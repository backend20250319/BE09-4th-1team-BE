package com.playvoice.commentservice.command.repository;

import com.playvoice.commentservice.command.entity.Unlike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UnlikeRepository extends JpaRepository<Unlike, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Unlike U WHERE U.comment.id = :commentId AND U.userId = :userId")
    int deleteByCommentIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);

    int countByCommentId(Long commentId);
}