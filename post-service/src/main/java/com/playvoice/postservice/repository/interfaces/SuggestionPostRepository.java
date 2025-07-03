package com.playvoice.postservice.repository.interfaces;

import com.playvoice.postservice.domain.SuggestionPost;

public interface SuggestionPostRepository {

    public SuggestionPost save(SuggestionPost suggestionPost);

    public SuggestionPost findById(Long id);

    public boolean checkLike(SuggestionPost suggestionPost, Long userId);

    public void saveLike(SuggestionPost suggestionPost, Long userId);

    public boolean checkUnlike(SuggestionPost suggestionPost, Long userId);

    public void saveUnlike(SuggestionPost suggestionPost, Long userId);
}
