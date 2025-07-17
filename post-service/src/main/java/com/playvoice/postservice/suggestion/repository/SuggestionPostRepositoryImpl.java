package com.playvoice.postservice.suggestion.repository;

import com.playvoice.postservice.like.entity.LikeEntity;
import com.playvoice.postservice.like.entity.UnlikeEntity;
import com.playvoice.postservice.like.repository.JpaLikeRepository;
import com.playvoice.postservice.like.repository.JpaUnlikeRepository;
import com.playvoice.postservice.suggestion.domain.SuggestionPost;
import com.playvoice.postservice.suggestion.entity.SuggestionPostEntity;
import jakarta.persistence.EntityManager;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SuggestionPostRepositoryImpl implements SuggestionPostRepository {

    private final JpaSuggestionPostRepository jpaSuggestionPostRepository;
    private final JpaLikeRepository jpaLikeRepository;
    private final JpaUnlikeRepository jpaUnlikeRepository;
    private final EntityManager entityManager;

    @Override
    public SuggestionPost save(SuggestionPost suggestionPost) {
        SuggestionPostEntity entity = new SuggestionPostEntity(suggestionPost);
        entity = jpaSuggestionPostRepository.save(entity);
        return entity.toSuggestionPost();
    }

    @Override
    public SuggestionPost findById(Long id) {
        SuggestionPostEntity entity = jpaSuggestionPostRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("건의 게시글을 찾을 수 없습니다."));

        return entity.toSuggestionPost();
    }

    @Override
    public void deleteById(Long id) {
        jpaSuggestionPostRepository.deleteById(id);
    }


    @Override
    public boolean checkLike(SuggestionPost suggestionPost, Long userId) {

        return jpaLikeRepository.existsById(new LikeEntity(suggestionPost, userId).getId());
    }

    @Override
    public void saveLike(SuggestionPost suggestionPost, Long userId) {
        jpaLikeRepository.save(new LikeEntity(suggestionPost, userId));
    }

    @Override
    public boolean checkUnlike(SuggestionPost suggestionPost, Long userId) {
        return jpaUnlikeRepository.existsById(new UnlikeEntity(suggestionPost, userId).getId());
    }

    @Override
    public void saveUnlike(SuggestionPost suggestionPost, Long userId) {
        jpaUnlikeRepository.save(new UnlikeEntity(suggestionPost, userId));
    }

    @Override
    public void deleteLike(SuggestionPost suggestionPost, Long userId) {
        jpaLikeRepository.deleteById(new LikeEntity(suggestionPost, userId).getId());
    }

    @Override
    public void deleteUnlike(SuggestionPost suggestionPost, Long userId) {
        jpaUnlikeRepository.deleteById(new LikeEntity(suggestionPost, userId).getId());
    }

    @Override
    public Page<SuggestionPost> findAll(Pageable pageable) {
        Page<SuggestionPostEntity> entities = jpaSuggestionPostRepository.findAll(pageable);

        return entities.map(SuggestionPostEntity::toSuggestionPost);
    }

    @Override
    public Page<SuggestionPost> findAllByUserId(Long userId, Pageable pageable) {
        Page<SuggestionPostEntity> entities = jpaSuggestionPostRepository.findAllByUserId(userId, pageable);
        return entities.map(SuggestionPostEntity::toSuggestionPost);
    }
}
