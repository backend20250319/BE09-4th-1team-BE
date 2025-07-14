package com.playvoice.postservice.suggestion.dto.response;

import com.playvoice.postservice.suggestion.domain.SuggestionPost;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GetSuggestionPostResponseDto(Long id, Long userId, String title, String content,
                                           Long likeCount, Long unlikeCount, Long views,
                                           Long commentCount,
                                           LocalDateTime createdAt, LocalDateTime updatedAt,
                                           boolean isAnswered, String username) {

    public static GetSuggestionPostResponseDto createDto(SuggestionPost suggestionPost,
        String username) {
        return GetSuggestionPostResponseDto.builder()
            .id(suggestionPost.getId())
            .userId(suggestionPost.getUserId())
            .title(suggestionPost.getTitle())
            .content(suggestionPost.getContent())
            .likeCount(suggestionPost.getLikeCount())
            .unlikeCount(suggestionPost.getUnlikeCount())
            .views(suggestionPost.getViewCount())
            .commentCount(suggestionPost.getCommentCount())
            .createdAt(suggestionPost.getCreatedAt())
            .updatedAt(suggestionPost.getUpdatedAt())
            .isAnswered(suggestionPost.isAnswered())
            .username(username)
            .build();
    }
}
