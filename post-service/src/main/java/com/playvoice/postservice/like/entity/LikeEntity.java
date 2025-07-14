package com.playvoice.postservice.like.entity;

import com.playvoice.postservice.answer.domain.AnswerPost;
import com.playvoice.postservice.suggestion.domain.SuggestionPost;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_like")
@NoArgsConstructor
@Getter
public class LikeEntity {

    @Id
    @Embedded
    private LikeEntityId id;

    public LikeEntity(SuggestionPost post, Long userId) {
        this.id = new LikeEntityId(post.getId(), userId, LikeTarget.SUGGESTION.name());
    }

    public LikeEntity(AnswerPost post, Long userId) {
        this.id = new LikeEntityId(post.getId(), userId, LikeTarget.ANSWER.name());
    }
}
