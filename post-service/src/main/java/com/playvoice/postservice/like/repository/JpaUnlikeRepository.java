package com.playvoice.postservice.like.repository;

import com.playvoice.postservice.like.entity.LikeEntityId;
import com.playvoice.postservice.like.entity.UnlikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUnlikeRepository extends JpaRepository<UnlikeEntity, LikeEntityId> {

}
