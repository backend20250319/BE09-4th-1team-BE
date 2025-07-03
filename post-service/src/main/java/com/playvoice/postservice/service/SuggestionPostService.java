package com.playvoice.postservice.service;

import com.playvoice.postservice.domain.SuggestionPost;
import com.playvoice.postservice.dto.request.CreateSuggestionPostRequestDto;
import com.playvoice.postservice.dto.request.LikeSuggestionPostRequestDto;
import com.playvoice.postservice.dto.request.UpdateSuggestionPostRequestDto;
import com.playvoice.postservice.dto.response.GetSuggestionPostResponseDto;
import com.playvoice.postservice.repository.interfaces.SuggestionPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SuggestionPostService {

    private final SuggestionPostRepository suggestionPostRepository;

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
        suggestionPost = suggestionPostRepository.save(suggestionPost);

        return GetSuggestionPostResponseDto.createDto(suggestionPost);
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
        }
        suggestionPostRepository.save(suggestionPost);
    }


}
