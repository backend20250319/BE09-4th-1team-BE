package com.playvoice.postservice.common.domain;

import com.playvoice.postservice.common.PositiveLongCounter;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class Post {

    private final Long id;
    private final Long userId;
    private String title;
    private String content;
    private final PositiveLongCounter likeCount;
    private final PositiveLongCounter unlikeCount;
    private final PositiveLongCounter views;
    private final PositiveLongCounter commentCount;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Post(Long id, Long userId, String title, String content, PositiveLongCounter likeCount,
        PositiveLongCounter unlikeCount, PositiveLongCounter views,
        PositiveLongCounter commentCount, LocalDateTime createdAt,
        LocalDateTime updatedAt) {

        if (userId == null) {
            throw new IllegalArgumentException("userId는 null이 될 수 없습니다.");
        }
        if (title.isEmpty()) {
            throw new IllegalArgumentException("제목이 비어있습니다.");
        }
        if (content.isEmpty()) {
            throw new IllegalArgumentException("내용이 비어있습니다.");
        }

        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.unlikeCount = unlikeCount;
        this.views = views;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 좋아요, 싫어요, 조회수 getter
    public Long getLikeCount() {
        return likeCount.getCount();
    }

    public Long getUnlikeCount() {
        return unlikeCount.getCount();
    }

    public Long getViewCount() {
        return views.getCount();
    }

    public Long getCommentCount() {
        return commentCount.getCount();
    }


    // 좋아요, 싫어요, 조회수 상승 및 감소
    public void increaseLikeCount() {
        likeCount.increase();
    }

    public void decreaseLikeCount() {
        likeCount.decrease();
    }

    public void increaseUnlikeCount() {
        unlikeCount.increase();
    }

    public void decreaseUnlikeCount() {
        unlikeCount.decrease();
    }

    public void increaseView() {
        views.increase();
    }

    public void increaseCommentCount() {
        commentCount.increase();
    }

    public void decreaseCommentCount() {
        commentCount.decrease();
    }

    protected void updatePost(Long userId, String title, String content) {
        if (userId.longValue() != this.userId.longValue()) {
            throw new IllegalArgumentException("userId가 일치하지 않습니다.");
        }

        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }


}
