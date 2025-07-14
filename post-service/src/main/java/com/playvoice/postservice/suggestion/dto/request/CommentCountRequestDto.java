package com.playvoice.postservice.suggestion.dto.request;

import jakarta.validation.constraints.NotNull;

public record CommentCountRequestDto(
    @NotNull Long userId,
    @NotNull Long suggestionPostId
) {

}
