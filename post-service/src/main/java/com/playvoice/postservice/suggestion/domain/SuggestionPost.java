package com.playvoice.postservice.suggestion.domain;

import com.playvoice.postservice.common.PositiveLongCounter;
import com.playvoice.postservice.common.domain.Post;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class SuggestionPost extends Post {

    private boolean isAnswered;

    // 새로운 SuggestionPost 생성 함수
    public static SuggestionPost createDefaultSuggestionPost(Long userId, String title,
        String content) {

        return SuggestionPost.builder()
            .id(null)
            .userId(userId)
            .title(title)
            .content(content)
            .likeCount(new PositiveLongCounter(0L))
            .unlikeCount(new PositiveLongCounter(0L))
            .views(new PositiveLongCounter(0L))
            .commentCount(new PositiveLongCounter(0L))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .isAnswered(false)
            .build();
    }

    // AllArgsConstructor
    public SuggestionPost(Long id, Long userId, String title, String content,
        PositiveLongCounter likeCount, PositiveLongCounter unlikeCount, PositiveLongCounter views,
        PositiveLongCounter commentCount, LocalDateTime createdAt, LocalDateTime updatedAt,
        boolean isAnswered) {
        super(id, userId, title, content, likeCount, unlikeCount, views, commentCount, createdAt,
            updatedAt);
        this.isAnswered = isAnswered;
    }


    public void addAnswerPost() {
        if (this.isAnswered) {
            throw new IllegalArgumentException("답변 여부가 이미 true 입니다.");
        }

        this.isAnswered = true;
    }

    public void deleteAnswerPost() {
        if (!this.isAnswered) {
            throw new IllegalArgumentException("답변 여부가 이미 false 입니다.");
        }

        this.isAnswered = false;
    }

    public void updateSuggestionPost(Long userId, String title, String content) {
        updatePost(userId, title, content);
    }
}
