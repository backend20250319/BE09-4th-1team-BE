package com.playvoice.postservice.answer.entity;

import com.playvoice.postservice.answer.domain.AnswerPost;
import com.playvoice.postservice.common.PositiveLongCounter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "tbl_answer_post")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class AnswerPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Long likeCount;
    private Long unlikeCount;
    private Long views;
    private Long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long suggestionPostId;

    public AnswerPostEntity(AnswerPost answerPost) {
        this.id = answerPost.getId();
        this.userId = answerPost.getUserId();
        this.title = answerPost.getTitle();
        this.content = answerPost.getContent();
        this.likeCount = answerPost.getLikeCount();
        this.unlikeCount = answerPost.getUnlikeCount();
        this.views = answerPost.getViewCount();
        this.commentCount = answerPost.getCommentCount();
        this.createdAt = answerPost.getCreatedAt();
        this.updatedAt = answerPost.getUpdatedAt();
        this.suggestionPostId = answerPost.getSuggestionPostId();
    }

    public AnswerPost toAnswerPost() {
        return AnswerPost.builder()
            .id(id)
            .userId(userId)
            .title(title)
            .content(content)
            .likeCount(new PositiveLongCounter(likeCount))
            .unlikeCount(new PositiveLongCounter(unlikeCount))
            .views(new PositiveLongCounter(views))
            .commentCount(new PositiveLongCounter(commentCount))
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .suggestionPostId(suggestionPostId)
            .build();
    }
}
