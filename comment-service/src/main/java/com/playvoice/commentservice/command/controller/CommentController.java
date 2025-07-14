package com.playvoice.commentservice.command.controller;

import com.playvoice.commentservice.command.dto.CommentCreateRequest;
import com.playvoice.commentservice.command.dto.CommentDTO;
import com.playvoice.commentservice.command.dto.CommentUpdateRequest;
import com.playvoice.commentservice.command.entity.ReactionStatus;
import com.playvoice.commentservice.command.service.CommentService;
import com.playvoice.commentservice.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Tag(name = "Comment API", description = "댓글 관련 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    /***
     * 댓글 생성
     * @param req
     * @return
     */
    @Operation(
            summary = "댓글 생성", description = "댓글을 생성합니다.", security = @SecurityRequirement(name = "Authorization"))
    @PostMapping
    public ResponseEntity<ApiResponse<CommentDTO>> createComment(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody CommentCreateRequest req) {
//        String userId = "1";

        System.out.println("userId = " + userId);

        CommentDTO commentDTO = commentService.createComment(req, Long.valueOf(userId));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(commentDTO));
    }

    /***
     * 댓글 조회
     * @param postId
     * @param page
     * @param size
     * @return
     */
    @Operation(
            summary = "댓글 조회", description = "댓글을 조회합니다.", security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Page<CommentDTO>>> getComments(
//            @AuthenticationPrincipal String userId,
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        String userId = "1";

        Page<CommentDTO> commentDTOList = commentService.getComments(userId, postId, page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(commentDTOList));
    }

    /***
     * 댓글 수정
     * @param commentId
     * @param req
     * @return
     */
    @Operation(
            summary = "댓글 수정", description = "댓글을 수정합니다.", security = @SecurityRequirement(name = "Authorization"))
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentDTO>> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest req
//        , @AuthenticationPrincipal String userId
    ) {
        String userId = "1";

        CommentDTO commentDTO = commentService.updateComment(req, commentId, Long.valueOf(userId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(commentDTO));
    }

    /***
     * 댓글 삭제
     * @param commentId
     * @return
     */
    @Operation(
            summary = "댓글 삭제",
            description = "댓글을 삭제합니다.",
            security = @SecurityRequirement(name = "Authorization"))
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId
//        , @AuthenticationPrincipal String userId
    ) {
        String userId = "1";

        commentService.deleteComment(commentId, Long.valueOf(userId));

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(null));
    }

    /***
     * 댓글 리액션 좋아요/싫어요
     * @param commentId
     * @param reactionStatus
     * @return
     */
    @Operation(
            summary = "댓글 리액션 업데이트",
            description = "댓글에 대한 사용자의 리액션 상태(좋아요, 싫어요, 취소)를 업데이트",
            security = @SecurityRequirement(name = "Authorization"))
    @PatchMapping("/{commentId}/reaction")
    public ResponseEntity<ApiResponse<CommentDTO>> updateReaction(
            @PathVariable Long commentId,
            @RequestParam("type") ReactionStatus reactionStatus
//        , @AuthenticationPrincipal String userId
    ) {
        String userId = "1";

        CommentDTO commentDTO = commentService.updateReaction(commentId, Long.parseLong(userId), reactionStatus);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(commentDTO));
    }
}
