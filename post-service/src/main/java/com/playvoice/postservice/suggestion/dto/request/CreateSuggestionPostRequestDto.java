package com.playvoice.postservice.suggestion.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateSuggestionPostRequestDto(
    @NotNull(message = "userId는 필수입니다.") Long userId,
    @NotEmpty(message = "title은 필수입니다.") String title,
    @NotEmpty(message = "content는 필수입니다.") String content) {

}
