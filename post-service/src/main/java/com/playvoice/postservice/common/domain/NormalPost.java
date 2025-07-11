package com.playvoice.postservice.common.domain;

import com.playvoice.postservice.common.PositiveLongCounter;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class NormalPost extends Post {

    public static NormalPost createDefaultNormalPost(Long userId, String title, String content) {
        return NormalPost.builder()
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
            .build();
    }

    public NormalPost(Long id, Long userId, String title, String content,
        PositiveLongCounter likeCount,
        PositiveLongCounter unlikeCount,
        PositiveLongCounter views, PositiveLongCounter commentCount, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        super(id, userId, title, content, likeCount, unlikeCount, views, commentCount, createdAt,
            updatedAt);
    }

    public void updateNormalPost(Long userId, String title, String content) {
        updatePost(userId, title, content);
    }
}
