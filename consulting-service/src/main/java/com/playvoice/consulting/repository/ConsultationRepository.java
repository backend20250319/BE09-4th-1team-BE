package com.playvoice.consulting.repository;

import com.playvoice.consulting.enitiy.ConsultationSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationRepository extends JpaRepository<ConsultationSession, Long>{
}
