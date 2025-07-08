package com.playvoice.consulting.repository;

import com.playvoice.consulting.dto.Status;
import com.playvoice.consulting.enitiy.ConsultationSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultationRepository extends JpaRepository<ConsultationSession, Long> {
    List<ConsultationSession> findByUserId(String userId);
    List<ConsultationSession> findByManagerId(String managerId);
    List<ConsultationSession> findByUserIdAndStatus(String userId, Status status);
    List<ConsultationSession> findByManagerIdAndStatus(String managerId, Status status);
}
