package com.playvoice.postservice.answer.repository;

import com.playvoice.postservice.answer.domain.AnswerPost;

public interface AnswerPostRepository {

    public AnswerPost save(AnswerPost answerPost);

    public AnswerPost findById(Long id);

    public void deleteById(Long id);

    public boolean checkLike(AnswerPost answerPost, Long userId);

    public void saveLike(AnswerPost answerPost, Long userId);

    public void saveUnlike(AnswerPost answerPost, Long userId);

    public boolean checkUnlike(AnswerPost answerPost, Long userId);

    void deleteLike(AnswerPost answerPost, Long userId);

    void deleteUnlike(AnswerPost answerPost, Long userId);

    AnswerPost findBySuggestionPostId(Long suggestionPostId);
}
