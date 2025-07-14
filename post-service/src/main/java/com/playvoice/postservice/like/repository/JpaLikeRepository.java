package com.playvoice.postservice.like.repository;

import com.playvoice.postservice.like.entity.LikeEntity;
import com.playvoice.postservice.like.entity.LikeEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLikeRepository extends JpaRepository<LikeEntity, LikeEntityId> {

}
