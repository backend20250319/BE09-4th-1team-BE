package com.playvoice.postservice.repository;

import com.playvoice.postservice.domain.SuggestionPost;
import com.playvoice.postservice.entity.LikeEntity;
import com.playvoice.postservice.entity.SuggestionPostEntity;
import com.playvoice.postservice.entity.UnlikeEntity;
import com.playvoice.postservice.repository.interfaces.SuggestionPostRepository;
import com.playvoice.postservice.repository.jpa.JpaLikeRepository;
import com.playvoice.postservice.repository.jpa.JpaSuggestionPostRepository;
import com.playvoice.postservice.repository.jpa.JpaUnlikeRepository;
import jakarta.persistence.EntityManager;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
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


}
