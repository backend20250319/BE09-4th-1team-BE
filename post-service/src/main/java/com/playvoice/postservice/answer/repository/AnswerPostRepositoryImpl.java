package com.playvoice.postservice.answer.repository;

import com.playvoice.postservice.answer.domain.AnswerPost;
import com.playvoice.postservice.answer.entity.AnswerPostEntity;
import com.playvoice.postservice.like.entity.LikeEntity;
import com.playvoice.postservice.like.entity.UnlikeEntity;
import com.playvoice.postservice.like.repository.JpaLikeRepository;
import com.playvoice.postservice.like.repository.JpaUnlikeRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnswerPostRepositoryImpl implements AnswerPostRepository {

    private final JpaAnswerPostRepository jpaAnswerPostRepository;
    private final JpaLikeRepository jpaLikeRepository;
    private final JpaUnlikeRepository jpaUnlikeRepository;

    @Override
    public AnswerPost save(AnswerPost answerPost) {
        AnswerPostEntity entity = new AnswerPostEntity(answerPost);
        entity = jpaAnswerPostRepository.save(entity);
        return entity.toAnswerPost();
    }

    @Override
    public AnswerPost findById(Long id) {
        AnswerPostEntity entity = jpaAnswerPostRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("답변 게시물을 찾지 못했습니다."));

        return entity.toAnswerPost();
    }

    @Override
    public void deleteById(Long id) {
        jpaAnswerPostRepository.deleteById(id);
    }

    @Override
    public boolean checkLike(AnswerPost answerPost, Long userId) {
        return jpaLikeRepository.existsById(new LikeEntity(answerPost, userId).getId());
    }

    @Override
    public void saveLike(AnswerPost answerPost, Long userId) {
        LikeEntity entity = new LikeEntity(answerPost, userId);
        jpaLikeRepository.save(entity);
    }

    @Override
    public boolean checkUnlike(AnswerPost answerPost, Long userId) {
        return jpaUnlikeRepository.existsById(new LikeEntity(answerPost, userId).getId());
    }

    @Override
    public void saveUnlike(AnswerPost answerPost, Long userId) {
        UnlikeEntity entity = new UnlikeEntity(answerPost, userId);
        jpaUnlikeRepository.save(entity);
    }

    @Override
    public void deleteLike(AnswerPost answerPost, Long userId) {
        LikeEntity entity = new LikeEntity(answerPost, userId);
        jpaLikeRepository.deleteById(entity.getId());
    }

    @Override
    public void deleteUnlike(AnswerPost answerPost, Long userId) {
        UnlikeEntity entity = new UnlikeEntity(answerPost, userId);
        jpaUnlikeRepository.deleteById(entity.getId());
    }

    @Override
    public AnswerPost findBySuggestionPostId(Long suggestionPostId) {
        AnswerPostEntity entity = jpaAnswerPostRepository.findBySuggestionPostId(suggestionPostId)
            .orElseThrow(() -> new NoSuchElementException("답변 게시물을 찾지 못했습니다."));
        return entity.toAnswerPost();
    }
}
