package com.playvoice.postservice.like.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LikeEntityId {

    private Long targetId;
    private Long userId;
    private String targetType;
}
