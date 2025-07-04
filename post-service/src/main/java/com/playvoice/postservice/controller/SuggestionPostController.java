package com.playvoice.postservice.controller;

import com.playvoice.postservice.common.ApiResponse;
import com.playvoice.postservice.dto.request.CreateSuggestionPostRequestDto;
import com.playvoice.postservice.dto.request.LikeSuggestionPostRequestDto;
import com.playvoice.postservice.dto.request.UpdateSuggestionPostRequestDto;
import com.playvoice.postservice.dto.response.GetSuggestionPostResponseDto;
import com.playvoice.postservice.service.SuggestionPostService;
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
}
