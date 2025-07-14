package com.playvoice.postservice.answer.domain;

import com.playvoice.postservice.common.PositiveLongCounter;
import com.playvoice.postservice.common.domain.Post;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class AnswerPost extends Post {

    private final Long suggestionPostId;

    // 새로운 AnswerPost 생성
    public static AnswerPost createDefaultAnswerPost(Long userId, String title, String content,
        Long suggestPostId) {
        return AnswerPost.builder()

            .id(null)
            .userId(userId)
            .title(title)
            .content(content)
            .suggestionPostId(suggestPostId)
            .likeCount(new PositiveLongCounter(0L))
            .unlikeCount(new PositiveLongCounter(0L))
            .views(new PositiveLongCounter(0L))
            .commentCount(new PositiveLongCounter(0L))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    // AllArgsConstructor
    public AnswerPost(Long id, Long userId, String title, String content,
        PositiveLongCounter likeCount,
        PositiveLongCounter unlikeCount,
        PositiveLongCounter views, PositiveLongCounter commentCount, LocalDateTime createdAt,
        LocalDateTime updatedAt, Long suggestPostId) {

        super(id, userId, title, content, likeCount, unlikeCount, commentCount, views, createdAt,
            updatedAt);
        if (suggestPostId == null) {
            throw new IllegalArgumentException("suggestPostId가 Null 입니다.");
        }
        this.suggestionPostId = suggestPostId;
    }

    public void updateAnswerPost(Long userId, String title, String content) {
        updatePost(userId, title, content);
    }
}
