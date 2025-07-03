package com.playvoice.postservice.repository.jpa;

import com.playvoice.postservice.entity.LikeEntity;
import com.playvoice.postservice.entity.LikeEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLikeRepository extends JpaRepository<LikeEntity, LikeEntityId> {

}
