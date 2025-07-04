package com.playvoice.postservice.service;

import com.playvoice.postservice.domain.AnswerPost;
import com.playvoice.postservice.domain.SuggestionPost;
import com.playvoice.postservice.dto.request.CreateAnswerPostRequestDto;
import com.playvoice.postservice.dto.request.LikeAnswerPostRequestDto;
import com.playvoice.postservice.dto.request.UpdateAnswerPostRequestDto;
import com.playvoice.postservice.dto.response.GetAnswerPostResponseDto;
import com.playvoice.postservice.repository.interfaces.AnswerPostRepository;
import com.playvoice.postservice.repository.interfaces.SuggestionPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerPostService {

    private final AnswerPostRepository answerPostRepository;
    private final SuggestionPostRepository suggestionPostRepository;


    @Transactional
    //TODO userId가 principal하고 같은지 체크 한번 해야될듯
    public Long createAnswerPost(CreateAnswerPostRequestDto dto) {
        AnswerPost answerPost = AnswerPost.createDefaultAnswerPost(dto.userId(), dto.title(),
            dto.content(), dto.suggestionPostId());

        SuggestionPost suggestionPost = suggestionPostRepository.findById(
            answerPost.getSuggestionPostId());
        if (suggestionPost.isAnswered()) {
            throw new IllegalArgumentException("이미 답변 게시물이 있습니다.");
        }

        suggestionPost.addAnswerPost();
        suggestionPostRepository.save(suggestionPost);
        answerPost = answerPostRepository.save(answerPost);

        return answerPost.getId();
    }

    @Transactional
    public GetAnswerPostResponseDto getAnswerPost(Long id) {
        AnswerPost answerPost = answerPostRepository.findById(id);
        answerPost.increaseView();
        answerPostRepository.save(answerPost);
        return GetAnswerPostResponseDto.createDto(answerPost);
    }

    @Transactional
    public void deleteAnswerPost(Long id) {
        //TODO userId 체크
        AnswerPost answerPost = answerPostRepository.findById(id);
        SuggestionPost suggestionPost = suggestionPostRepository.findById(
            answerPost.getSuggestionPostId());

        suggestionPost.deleteAnswerPost();

        suggestionPostRepository.save(suggestionPost);
        answerPostRepository.deleteById(answerPost.getId());
    }

    @Transactional
    public Long updateAnswerPost(Long id, UpdateAnswerPostRequestDto dto) {
        AnswerPost answerPost = answerPostRepository.findById(id);
        //TODO UserID 체크
        answerPost.updateAnswerPost(dto.userId(), dto.title(), dto.content());
        answerPost = answerPostRepository.save(answerPost);

        return answerPost.getId();
    }

    @Transactional
    public void likeAnswerPost(LikeAnswerPostRequestDto dto) {
        //TODO userId 체크
        AnswerPost answerPost = answerPostRepository.findById(dto.answerPostId());

        if (!answerPostRepository.checkLike(answerPost, dto.userId())) {
            answerPost.increaseLikeCount();
            answerPostRepository.saveLike(answerPost, dto.userId());
        } else {
            answerPost.decreaseLikeCount();
            answerPostRepository.deleteLike(answerPost, dto.userId());
        }

        answerPostRepository.save(answerPost);
    }

    @Transactional
    public void unlikeAnswerPost(LikeAnswerPostRequestDto dto) {
        //TODO userId 체크
        AnswerPost answerPost = answerPostRepository.findById(dto.answerPostId());

        if (!answerPostRepository.checkUnlike(answerPost, dto.userId())) {
            answerPost.increaseUnlikeCount();
            answerPostRepository.saveUnlike(answerPost, dto.userId());
        } else {
            answerPost.decreaseUnlikeCount();
            answerPostRepository.deleteUnlike(answerPost, dto.userId());
        }

        answerPostRepository.save(answerPost);
    }
}
