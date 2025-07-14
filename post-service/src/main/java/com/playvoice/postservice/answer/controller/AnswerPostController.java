package com.playvoice.postservice.answer.controller;

import com.playvoice.postservice.answer.dto.request.CreateAnswerPostRequestDto;
import com.playvoice.postservice.answer.dto.request.LikeAnswerPostRequestDto;
import com.playvoice.postservice.answer.dto.request.UpdateAnswerPostRequestDto;
import com.playvoice.postservice.answer.dto.response.GetAnswerPostResponseDto;
import com.playvoice.postservice.answer.service.AnswerPostService;
import com.playvoice.postservice.common.ApiResponse;
import com.playvoice.postservice.suggestion.dto.request.CommentCountRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answerPost")
@RequiredArgsConstructor
public class AnswerPostController {

    private final AnswerPostService answerPostService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createAnswerPost(
        @Valid @RequestBody CreateAnswerPostRequestDto dto) {
        System.out.println(dto);

        Long id = answerPostService.createAnswerPost(dto);

        return ResponseEntity.ok(ApiResponse.success(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetAnswerPostResponseDto>> getAnswerPost(
        @PathVariable Long id) {

        GetAnswerPostResponseDto dto = answerPostService.getAnswerPost(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/by-suggestion/{suggestionPostId}")
    public ResponseEntity<ApiResponse<GetAnswerPostResponseDto>> getAnswerPostBySuggestionPostId(@PathVariable Long suggestionPostId) {
        GetAnswerPostResponseDto dto = answerPostService.getAnswerPostBySuggestionPostId(suggestionPostId);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> updateAnswerPost(@PathVariable Long id,
        @Valid @RequestBody UpdateAnswerPostRequestDto dto) {
        id = answerPostService.updateAnswerPost(id, dto);

        return ResponseEntity.ok(ApiResponse.success(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAnswerPost(@PathVariable Long id) {
        answerPostService.deleteAnswerPost(id);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/like")
    public ResponseEntity<ApiResponse<Void>> likeAnswerPost(
        @RequestBody LikeAnswerPostRequestDto dto) {
        answerPostService.likeAnswerPost(dto);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/unlike")
    public ResponseEntity<ApiResponse<Void>> unlikeAnswerPost(
        @RequestBody LikeAnswerPostRequestDto dto) {
        answerPostService.unlikeAnswerPost(dto);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/increaseComment")
    public ResponseEntity<ApiResponse<Void>> increaseCommentCount(
        @RequestBody CommentCountRequestDto dto) {

        //TODO 뭔가 확인하는 로직
        answerPostService.increaseCommentCount(dto);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/decreaseComment")
    public ResponseEntity<ApiResponse<Void>> decreaseCommentCount(
        @RequestBody CommentCountRequestDto dto) {
        answerPostService.decreaseCommentCount(dto);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
