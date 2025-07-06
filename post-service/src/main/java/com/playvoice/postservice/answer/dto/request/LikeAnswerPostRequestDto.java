package com.playvoice.postservice.answer.dto.request;

import jakarta.validation.constraints.NotNull;

public record LikeAnswerPostRequestDto(
    @NotNull(message = "userId는 필수입니다.") Long userId,
    @NotNull(message = "suggestPostId는 필수입니다.") Long answerPostId
) {

}
