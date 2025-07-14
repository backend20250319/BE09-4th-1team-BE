package com.playvoice.postservice.common.domain;

import com.playvoice.postservice.common.PositiveLongCounter;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class NoticePost extends Post {

    private boolean isImportant;

    // 새로운 NoticePost 생성 함수
    public static NoticePost createDefaultNoticePost(Long userId, String title, String content,
        boolean isImportant) {
        return NoticePost.builder()
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
            .isImportant(isImportant)
            .build();
    }

    // AllArgsConstructor
    public NoticePost(Long id, Long userId, String title, String content,
        PositiveLongCounter likeCount,
        PositiveLongCounter unlikeCount,
        PositiveLongCounter views, PositiveLongCounter commentCount, LocalDateTime createdAt,
        LocalDateTime updatedAt, boolean isImportant) {
        super(id, userId, title, content, likeCount, unlikeCount, views, commentCount, createdAt,
            updatedAt);
        this.isImportant = isImportant;
    }

    public void updateNoticePost(Long userId, String title, String content, boolean isImportant) {
        updatePost(userId, title, content);
        this.isImportant = isImportant;
    }
}
