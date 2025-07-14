package com.playvoice.postservice.answer.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateAnswerPostRequestDto(
    @NotNull(message = "userId는 필수입니다.") Long userId,
    @NotEmpty(message = "title이 필수입니다.") String title,
    @NotEmpty(message = "userId는 필수입니다.") String content
) {

}
