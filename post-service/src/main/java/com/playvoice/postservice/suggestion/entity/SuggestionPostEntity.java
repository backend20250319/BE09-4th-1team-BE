package com.playvoice.postservice.suggestion.entity;

import com.playvoice.postservice.common.PositiveLongCounter;
import com.playvoice.postservice.suggestion.domain.SuggestionPost;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_suggestion_post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SuggestionPostEntity {

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
    private boolean isAnswered;


    public SuggestionPostEntity(SuggestionPost suggestionPost) {
        this.id = suggestionPost.getId();
        this.userId = suggestionPost.getUserId();
        this.title = suggestionPost.getTitle();
        this.content = suggestionPost.getContent();
        this.likeCount = suggestionPost.getLikeCount();
        this.unlikeCount = suggestionPost.getUnlikeCount();
        this.views = suggestionPost.getViewCount();
        this.commentCount = suggestionPost.getCommentCount();
        this.createdAt = suggestionPost.getCreatedAt();
        this.updatedAt = suggestionPost.getUpdatedAt();
        this.isAnswered = suggestionPost.isAnswered();
    }

    public SuggestionPost toSuggestionPost() {
        return new SuggestionPost(id, userId, title, content, new PositiveLongCounter(likeCount),
            new PositiveLongCounter(unlikeCount), new PositiveLongCounter(views),
            new PositiveLongCounter(commentCount),
            createdAt, updatedAt, isAnswered);
    }
}
