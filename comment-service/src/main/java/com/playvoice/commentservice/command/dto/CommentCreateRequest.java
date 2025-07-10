package com.playvoice.commentservice.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentCreateRequest {
    @Schema(description = "게시글 ID", example = "1")
    private Long postId;

    @Schema(description = "부모 댓글 ID", example = "null")
    private Long parentId;

    @Schema(description = "댓글 내용", example = "이 글 정말 도움이 됐어요!")
    private String content;
}
