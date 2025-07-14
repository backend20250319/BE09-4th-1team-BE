package com.playvoice.postservice.answer.dto.response;

import com.playvoice.postservice.answer.domain.AnswerPost;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GetAnswerPostResponseDto(
    Long id, Long userId, String title, String content,
    Long likeCount, Long unlikeCount, Long views,
    Long commentCount,
    LocalDateTime createdAt, LocalDateTime updatedAt,
    Long suggestionPostId,
    String username
) {

    public static GetAnswerPostResponseDto createDto(AnswerPost answerPost, String username) {
        return GetAnswerPostResponseDto.builder()
            .id(answerPost.getId())
            .userId(answerPost.getUserId())
            .title(answerPost.getTitle())
            .content(answerPost.getContent())
            .likeCount(answerPost.getLikeCount())
            .unlikeCount(answerPost.getUnlikeCount())
            .views(answerPost.getViewCount())
            .commentCount(answerPost.getCommentCount())
            .createdAt(answerPost.getCreatedAt())
            .updatedAt(answerPost.getUpdatedAt())
            .suggestionPostId(answerPost.getSuggestionPostId())
            .username(username)
            .build();
    }
}
