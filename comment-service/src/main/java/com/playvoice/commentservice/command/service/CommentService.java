package com.playvoice.commentservice.command.service;

import com.playvoice.commentservice.command.dto.AuthorDTO;
import com.playvoice.commentservice.command.dto.CommentCreateRequest;
import com.playvoice.commentservice.command.dto.CommentDTO;
import com.playvoice.commentservice.command.dto.CommentUpdateRequest;
import com.playvoice.commentservice.command.entity.Comment;
import com.playvoice.commentservice.command.entity.Like;
import com.playvoice.commentservice.command.entity.ReactionStatus;
import com.playvoice.commentservice.command.entity.Unlike;
import com.playvoice.commentservice.command.repository.CommentRepository;
import com.playvoice.commentservice.command.repository.LikeRepository;
import com.playvoice.commentservice.command.repository.UnlikeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UnlikeRepository unlikeRepository;
//    private final PostClient postClient;
//    private final UserClient userClient;

    // 댓글 생성
    @Transactional
    public CommentDTO createComment(CommentCreateRequest req, Long userId) {

        // TODO - userId로 유저 정보 받아오기 > author
        // TODO - 부모 댓글의 parentId는 null 이어야 함

        // Comment Entity 생성
        Comment comment = Comment.builder()
                .userId(userId)
                .postId(req.getPostId())
                .parentId(req.getParentId())
                .content(req.getContent())
                .build();

        // 저장
        Comment savedComment = commentRepository.save(comment);

        // DTO 반환
        return CommentDTO.builder()
                .commentId(savedComment.getId())
                .postId(savedComment.getPostId())
                .author(AuthorDTO.builder()
                        .userId(userId)
                        .name("test name")
                        .build())
                .content(savedComment.getContent())
                .parentId(savedComment.getParentId())
                .likeCount(0)
                .unlikeCount(0)
                .myReaction(ReactionStatus.NONE)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public Page<CommentDTO> getComments(String userId, Long postId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // postId 기준 댓글 목록 조회 (페이징)
        Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);

        return commentPage.map(comment -> {
            int likeCount = comment.getLikes() != null ? comment.getLikes().size() : 0;
            int unlikeCount = comment.getUnlikes() != null ? comment.getUnlikes().size() : 0;

            // 좋아요/싫어요
            ReactionStatus myReaction = ReactionStatus.NONE;
            Long currentUserId = Long.parseLong(userId);
            if (comment.getLikes().stream().anyMatch(like -> like.getUserId().equals(currentUserId))) {
                myReaction = ReactionStatus.LIKE;
            } else if (comment.getUnlikes().stream().anyMatch(unlike -> unlike.getUserId().equals(currentUserId))) {
                myReaction = ReactionStatus.UNLIKE;
            }

            return CommentDTO.builder()
                    .commentId(comment.getId())
                    .postId(comment.getPostId())
                    .author(AuthorDTO.builder()
                            .userId(comment.getUserId())
                            .name("사용자 이름") // TODO: UserClient 연동
                            .build())
                    .content(comment.getContent())
                    .parentId(comment.getParentId())
                    .likeCount(likeCount)
                    .unlikeCount(unlikeCount)
                    .myReaction(myReaction)
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        });
    }

    @Transactional
    public CommentDTO updateComment(@Valid CommentUpdateRequest req, Long commentId, Long userId) {
        // 1. 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 2. 작성자 확인
        if (!comment.getUserId().equals(userId)) {
            throw new SecurityException("본인의 댓글만 수정할 수 있습니다.");
        }

        // 3. 내용 수정
        comment.setContent(req.getContent());
        commentRepository.save(comment);

        // 좋아요/싫어요
        ReactionStatus myReaction = ReactionStatus.NONE;
        if (comment.getLikes().stream().anyMatch(like -> like.getUserId().equals(userId))) {
            myReaction = ReactionStatus.LIKE;
        } else if (comment.getUnlikes().stream().anyMatch(unlike -> unlike.getUserId().equals(userId))) {
            myReaction = ReactionStatus.UNLIKE;
        }

        // 4. DTO 변환 후 반환
        return CommentDTO.builder()
                .commentId(comment.getId())
                .postId(comment.getPostId())
                .author(AuthorDTO.builder()
                        .userId(comment.getUserId())
                        .name("사용자 이름") // TODO: 유저 서비스 연동
                        .build())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .likeCount(comment.getLikes() != null ? comment.getLikes().size() : 0)
                .unlikeCount(comment.getLikes() != null ? comment.getUnlikes().size() : 0)
                .myReaction(myReaction)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getUserId().equals(userId)) {
            throw new SecurityException("본인의 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }


    @Transactional
    public CommentDTO updateReaction(Long commentId, long userId, ReactionStatus reactionStatus) {

        // 1. 댓글 존재 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 2. 기존 리액션 제거
        likeRepository.deleteByCommentIdAndUserId(commentId, userId);
        unlikeRepository.deleteByCommentIdAndUserId(commentId, userId);

        // 3. 새 리액션 등록 (LIKE or UNLIKE)
        if (reactionStatus == ReactionStatus.LIKE) {
            likeRepository.save(Like.builder()
                    .userId(userId)
                    .comment(comment)
                    .build());
        } else if (reactionStatus == ReactionStatus.UNLIKE) {
            unlikeRepository.save(Unlike.builder()
                    .userId(userId)
                    .comment(comment)
                    .build());
        }

        // ✅ 4. DB에서 최신 리액션 수 조회
        int likeCount = likeRepository.countByCommentId(commentId);
        int unlikeCount = unlikeRepository.countByCommentId(commentId);

        // 5. DTO 변환 후 반환
        return CommentDTO.builder()
                .commentId(comment.getId())
                .postId(comment.getPostId())
                .author(AuthorDTO.builder()
                        .userId(comment.getUserId())
                        .name("사용자 이름") // TODO: 유저 서비스 연동
                        .build())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .likeCount(likeCount)       // ✅ 실제 최신 값으로 반영
                .unlikeCount(unlikeCount)   // ✅ 실제 최신 값으로 반영
                .myReaction(reactionStatus)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
