package com.playvoice.postservice.suggestion.dto.request;

import jakarta.validation.constraints.NotNull;

public record LikeSuggestionPostRequestDto(
    @NotNull(message = "userId는 필수입니다.") Long userId,
    @NotNull(message = "suggestPostId는 필수입니다.") Long suggestionPostId
) {

}
