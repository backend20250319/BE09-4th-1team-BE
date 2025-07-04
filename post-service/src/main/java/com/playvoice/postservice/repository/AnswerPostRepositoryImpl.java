package com.playvoice.postservice.repository;

import com.playvoice.postservice.domain.AnswerPost;
import com.playvoice.postservice.entity.AnswerPostEntity;
import com.playvoice.postservice.entity.LikeEntity;
import com.playvoice.postservice.entity.UnlikeEntity;
import com.playvoice.postservice.repository.interfaces.AnswerPostRepository;
import com.playvoice.postservice.repository.jpa.JpaAnswerPostRepository;
import com.playvoice.postservice.repository.jpa.JpaLikeRepository;
import com.playvoice.postservice.repository.jpa.JpaUnlikeRepository;
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
}
