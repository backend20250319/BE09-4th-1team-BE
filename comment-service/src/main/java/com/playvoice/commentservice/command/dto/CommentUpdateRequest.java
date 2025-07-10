package com.playvoice.commentservice.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentUpdateRequest {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
}
