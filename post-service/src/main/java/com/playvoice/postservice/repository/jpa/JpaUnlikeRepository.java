package com.playvoice.postservice.repository.jpa;

import com.playvoice.postservice.entity.LikeEntityId;
import com.playvoice.postservice.entity.UnlikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUnlikeRepository extends JpaRepository<UnlikeEntity, LikeEntityId> {

}
