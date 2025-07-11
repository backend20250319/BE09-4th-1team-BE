package com.playvoice.postservice.answer.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateAnswerPostRequestDto(
    @NotNull(message = "userId는 필수입니다.") Long userId,
    @NotNull(message = "suggestionId는 필수입니다.") Long suggestionPostId,
    @NotEmpty(message = "title은 필수입니다.") String title,
    @NotEmpty(message = "content는 필수입니다.") String content
) {

}
