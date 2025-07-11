package com.playvoice.postservice.answer.dto.request;

import jakarta.validation.constraints.NotNull;

public record CommentCountRequestDto(
    @NotNull Long userId,
    @NotNull Long answerPostId
) {

}
