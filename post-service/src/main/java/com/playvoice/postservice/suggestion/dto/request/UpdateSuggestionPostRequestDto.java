package com.playvoice.postservice.suggestion.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateSuggestionPostRequestDto(
    @NotNull(message = "userId는 필수입니다.") Long userId,
    @NotEmpty(message = "title이 필수입니다.") String title,
    @NotEmpty(message = "userId는 필수입니다.") String content) {

}
