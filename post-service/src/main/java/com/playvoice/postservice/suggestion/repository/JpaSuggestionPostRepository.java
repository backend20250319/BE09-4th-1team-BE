package com.playvoice.postservice.suggestion.repository;

import com.playvoice.postservice.suggestion.entity.SuggestionPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSuggestionPostRepository extends JpaRepository<SuggestionPostEntity, Long> {

}
