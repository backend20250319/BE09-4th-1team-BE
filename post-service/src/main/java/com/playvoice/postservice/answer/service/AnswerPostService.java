package com.playvoice.postservice.answer.service;

import com.playvoice.postservice.answer.domain.AnswerPost;
import com.playvoice.postservice.answer.dto.request.CreateAnswerPostRequestDto;
import com.playvoice.postservice.answer.dto.request.LikeAnswerPostRequestDto;
import com.playvoice.postservice.answer.dto.request.UpdateAnswerPostRequestDto;
import com.playvoice.postservice.answer.dto.response.GetAnswerPostResponseDto;
import com.playvoice.postservice.answer.repository.AnswerPostRepository;
import com.playvoice.postservice.fegin.UserServiceClient;
import com.playvoice.postservice.suggestion.domain.SuggestionPost;
import com.playvoice.postservice.suggestion.dto.request.CommentCountRequestDto;
import com.playvoice.postservice.suggestion.repository.SuggestionPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerPostService {

    private final AnswerPostRepository answerPostRepository;
    private final SuggestionPostRepository suggestionPostRepository;
    private final UserServiceClient userServiceClient;


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
        String username = userServiceClient.getUserById(answerPost.getUserId()).name();

        return GetAnswerPostResponseDto.createDto(answerPost, username);
    }

    @Transactional(readOnly = true)
    public GetAnswerPostResponseDto getAnswerPostBySuggestionPostId(Long suggestionPostId) {
        AnswerPost answerPost = answerPostRepository.findBySuggestionPostId(suggestionPostId);
        String username = userServiceClient.getUserById(answerPost.getUserId()).username();
        return GetAnswerPostResponseDto.createDto(answerPost, username);
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

    public void increaseCommentCount(CommentCountRequestDto dto) {
        // TODO 확인 로직
        AnswerPost answerPost = answerPostRepository.findById(dto.suggestionPostId());
        answerPost.increaseCommentCount();
        answerPostRepository.save(answerPost);
    }

    public void decreaseCommentCount(CommentCountRequestDto dto) {
        AnswerPost answerPost = answerPostRepository.findById(dto.suggestionPostId());
        answerPost.decreaseCommentCount();
        answerPostRepository.save(answerPost);
    }
}
