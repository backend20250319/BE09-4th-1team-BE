package com.playvoice.postservice.repository.jpa;

import com.playvoice.postservice.entity.SuggestionPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSuggestionPostRepository extends JpaRepository<SuggestionPostEntity, Long> {

}
