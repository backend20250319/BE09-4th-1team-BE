package com.playvoice.postservice.suggestion.controller;

import com.playvoice.postservice.common.ApiResponse;
import com.playvoice.postservice.suggestion.dto.request.CommentCountRequestDto;
import com.playvoice.postservice.suggestion.dto.request.CreateSuggestionPostRequestDto;
import com.playvoice.postservice.suggestion.dto.request.LikeSuggestionPostRequestDto;
import com.playvoice.postservice.suggestion.dto.request.UpdateSuggestionPostRequestDto;
import com.playvoice.postservice.suggestion.dto.response.GetSuggestionPostResponseDto;
import com.playvoice.postservice.suggestion.service.SuggestionPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suggestionPost")
public class SuggestionPostController {

    private final SuggestionPostService suggestionPostService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createSuggestionPost(@Valid @RequestBody
    CreateSuggestionPostRequestDto requestDto) {
        Long id = suggestionPostService.createSuggestionPost(requestDto);

        return ResponseEntity.ok(ApiResponse.success(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetSuggestionPostResponseDto>> getSuggestionPost(
        @PathVariable Long id) {
        GetSuggestionPostResponseDto dto = suggestionPostService.getSuggestionPost(id);

        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> updateSuggestionPost(
        @PathVariable Long id, @Valid @RequestBody UpdateSuggestionPostRequestDto dto) {

        id = suggestionPostService.updateSuggestionPost(id, dto);
        return ResponseEntity.ok(ApiResponse.success(id));
    }

    @PostMapping("/like")
    public ResponseEntity<ApiResponse<Void>> likeSuggestionPost(@Valid @RequestBody
    LikeSuggestionPostRequestDto dto) {
        System.out.println(dto.suggestionPostId());
        suggestionPostService.likeSuggestion(dto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/unlike")
    public ResponseEntity<ApiResponse<Void>> unlikeSuggestionPost(@Valid @RequestBody
    LikeSuggestionPostRequestDto dto) {
        suggestionPostService.unlikeSuggestion(dto);
        return ResponseEntity.ok(ApiResponse.success(null));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSuggestionPost(@PathVariable Long id) {
        suggestionPostService.deleteSuggestion(id);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/increaseComment")
    public ResponseEntity<ApiResponse<Void>> increaseCommentCount(
        @RequestBody CommentCountRequestDto dto) {

        //TODO 뭔가 확인하는 로직
        suggestionPostService.increaseCommentCount(dto);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/decreaseComment")
    public ResponseEntity<ApiResponse<Void>> decreaseCommentCount(
        @RequestBody CommentCountRequestDto dto) {

        suggestionPostService.decreaseCommentCount(dto);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<GetSuggestionPostResponseDto>>> getSuggestionPosts(
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GetSuggestionPostResponseDto> suggestionPosts = suggestionPostService.getAllSuggestions(
            pageable);
        return ResponseEntity.ok(ApiResponse.success(suggestionPosts));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<GetSuggestionPostResponseDto>>> getMySuggestionPosts(
        @org.springframework.web.bind.annotation.RequestParam("userId") Long userId,
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GetSuggestionPostResponseDto> suggestionPosts = suggestionPostService.getAllSuggestionsByUserId(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(suggestionPosts));
    }
}
