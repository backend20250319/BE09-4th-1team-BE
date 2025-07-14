package com.playvoice.commentservice.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentCountDTO {

    @Schema(description = "댓글 총 개수", example = "15")
    private Long count;
}