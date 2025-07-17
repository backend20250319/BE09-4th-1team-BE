package com.playvoice.postservice.suggestion.service;

import com.playvoice.postservice.fegin.UserServiceClient;
import com.playvoice.postservice.fegin.dto.UserResponseDTO;
import com.playvoice.postservice.suggestion.domain.SuggestionPost;
import com.playvoice.postservice.suggestion.dto.request.CommentCountRequestDto;
import com.playvoice.postservice.suggestion.dto.request.CreateSuggestionPostRequestDto;
import com.playvoice.postservice.suggestion.dto.request.LikeSuggestionPostRequestDto;
import com.playvoice.postservice.suggestion.dto.request.UpdateSuggestionPostRequestDto;
import com.playvoice.postservice.suggestion.dto.response.GetSuggestionPostResponseDto;
import com.playvoice.postservice.suggestion.repository.SuggestionPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SuggestionPostService {

    private final SuggestionPostRepository suggestionPostRepository;
    private final UserServiceClient userServiceClient;

    @Transactional
    public Long createSuggestionPost(CreateSuggestionPostRequestDto requestDto) {
        SuggestionPost suggestionPost = SuggestionPost.createDefaultSuggestionPost(
            requestDto.userId(), requestDto.title(), requestDto.content());

        // TODO 해당 유저가 있어야 생성 가능하고 인증 정보가 유저랑 같아야함
        suggestionPost = suggestionPostRepository.save(suggestionPost);

        return suggestionPost.getId();
    }

    @Transactional
    public GetSuggestionPostResponseDto getSuggestionPost(Long id) {
        SuggestionPost suggestionPost = suggestionPostRepository.findById(id);
        suggestionPost.increaseView();
        UserResponseDTO userResponseDTO = userServiceClient.getUserById(suggestionPost.getUserId());
        suggestionPost = suggestionPostRepository.save(suggestionPost);

        return GetSuggestionPostResponseDto.createDto(suggestionPost, userResponseDTO.name());
    }

    @Transactional
    public Long updateSuggestionPost(Long id, UpdateSuggestionPostRequestDto dto) {
        SuggestionPost suggestionPost = suggestionPostRepository.findById(id);
        suggestionPost.updateSuggestionPost(dto.userId(), dto.title(), dto.content());
        suggestionPostRepository.save(suggestionPost);

        return suggestionPost.getId();
    }

    @Transactional
    public void likeSuggestion(LikeSuggestionPostRequestDto dto) {
        SuggestionPost suggestionPost = suggestionPostRepository.findById(dto.suggestionPostId());

        // TODO 여기서 유저 확인

        if (!suggestionPostRepository.checkLike(suggestionPost, dto.userId())) {
            suggestionPost.increaseLikeCount();
            suggestionPostRepository.saveLike(suggestionPost, dto.userId());
        } else {
            suggestionPost.decreaseLikeCount();
            suggestionPostRepository.deleteLike(suggestionPost, dto.userId());
        }

        suggestionPostRepository.save(suggestionPost);
    }

    @Transactional
    public void unlikeSuggestion(LikeSuggestionPostRequestDto dto) {
        SuggestionPost suggestionPost = suggestionPostRepository.findById(dto.suggestionPostId());

        // TODO 여기서 유저 확인

        if (!suggestionPostRepository.checkUnlike(suggestionPost, dto.userId())) {
            suggestionPost.increaseUnlikeCount();
            suggestionPostRepository.saveUnlike(suggestionPost, dto.userId());
        } else {
            suggestionPost.decreaseUnlikeCount();
            suggestionPostRepository.deleteUnlike(suggestionPost, dto.userId());
        }
        suggestionPostRepository.save(suggestionPost);
    }


    public void deleteSuggestion(Long id) {

        //TODO 여기서 userID 체크 한번 들어갸아됌
        suggestionPostRepository.deleteById(id);
    }

    @Transactional
    public void increaseCommentCount(CommentCountRequestDto dto) {
        SuggestionPost suggestionPost = suggestionPostRepository.findById(dto.suggestionPostId());
        suggestionPost.increaseCommentCount();
        suggestionPostRepository.save(suggestionPost);
    }

    @Transactional
    public void decreaseCommentCount(CommentCountRequestDto dto) {
        SuggestionPost suggestionPost = suggestionPostRepository.findById(dto.suggestionPostId());
        suggestionPost.decreaseCommentCount();
        suggestionPostRepository.save(suggestionPost);
    }

    public Page<GetSuggestionPostResponseDto> getAllSuggestions(Pageable pageable) {
        return suggestionPostRepository.findAll(pageable)
            .map((suggestionPost -> {
                String username = userServiceClient.getUserById(suggestionPost.getUserId())
                    .name();
                return GetSuggestionPostResponseDto.createDto(suggestionPost, username);
            }));
    }

    public Page<GetSuggestionPostResponseDto> getAllSuggestionsByUserId(Long userId, Pageable pageable) {
        return suggestionPostRepository.findAllByUserId(userId, pageable)
            .map((suggestionPost -> {
                String username = userServiceClient.getUserById(suggestionPost.getUserId())
                    .name();
                return GetSuggestionPostResponseDto.createDto(suggestionPost, username);
            }));
    }
}
