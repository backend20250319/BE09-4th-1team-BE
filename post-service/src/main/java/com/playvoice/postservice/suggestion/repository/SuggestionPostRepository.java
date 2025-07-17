package com.playvoice.postservice.suggestion.repository;

import com.playvoice.postservice.suggestion.domain.SuggestionPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SuggestionPostRepository {

    public SuggestionPost save(SuggestionPost suggestionPost);

    public SuggestionPost findById(Long id);

    public boolean checkLike(SuggestionPost suggestionPost, Long userId);

    public void saveLike(SuggestionPost suggestionPost, Long userId);

    public boolean checkUnlike(SuggestionPost suggestionPost, Long userId);

    public void saveUnlike(SuggestionPost suggestionPost, Long userId);

    public void deleteById(Long id);

    public void deleteLike(SuggestionPost suggestionPost, Long userId);

    public void deleteUnlike(SuggestionPost suggestionPost, Long userId);

    Page<SuggestionPost> findAll(Pageable pageable);
    Page<SuggestionPost> findAllByUserId(Long userId, Pageable pageable);
}
