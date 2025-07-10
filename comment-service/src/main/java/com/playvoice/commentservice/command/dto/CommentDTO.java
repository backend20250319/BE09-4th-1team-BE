package com.playvoice.commentservice.command.dto;

import com.playvoice.commentservice.command.entity.ReactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDTO {

    @Schema(description = "댓글 ID", example = "1")
    private Long commentId;

    @Schema(description = "게시글 ID", example = "1")
    private Long postId;

    @Schema(description = "작성자 정보")
    private AuthorDTO author;

    @Schema(description = "댓글 내용", example = "이 글 정말 도움이 됐어요!")
    private String content;

    @Schema(description = "부모 댓글 ID (댓글일 경우 null)", example = "null")
    private Long parentId;

    @Schema(description = "좋아요 수", example = "3")
    private int likeCount;

    @Schema(description = "싫어요 수", example = "0")
    private int unlikeCount;

    @Schema(description = "내 리액션 상태", example = "LIKE")
    private ReactionStatus myReaction;

    @Schema(description = "댓글 생성 시각", example = "2025-06-27T14:03:00")
    private LocalDateTime createdAt;

    @Schema(description = "댓글 수정 시각", example = "2025-06-27T14:06:00")
    private LocalDateTime updatedAt;
}
