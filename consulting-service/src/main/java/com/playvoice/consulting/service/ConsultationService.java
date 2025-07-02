package com.playvoice.consulting.service;

import com.playvoice.consulting.dto.ConsultationDetailsDto;
import com.playvoice.consulting.dto.ConsultationFeedbackDto;
import com.playvoice.consulting.enitiy.ConsultationSession;
import com.playvoice.consulting.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;

    @Transactional(readOnly = true)
    public ConsultationDetailsDto getConsultationDetails(Long sessionId) {
        Optional<ConsultationSession> sessionOptional = consultationRepository.findById(sessionId);
        return sessionOptional.map(this::mapToDto).orElse(null);
    }

    @Transactional
    public Long createNewSession(String userId, String managerId, LocalDateTime dateTime) {
        ConsultationSession newSession = new ConsultationSession();
        newSession.setUserId(userId);
        newSession.setManagerId(managerId);
        newSession.setLocalDateTime(dateTime);
        newSession.setConsultationDate(dateTime.toLocalDate());
        newSession.setConsultationText(null); // 초기에는 null로 설정하거나 생략 (DB default 값에 따름)
        newSession.setReview(null);         // 초기에는 null로 설정하거나 생략 (DB default 값에 따름)

        ConsultationSession savedSession = consultationRepository.save(newSession);

        return savedSession.getSessionId();
    }

    @Transactional
    public boolean submitFeedbackAndReview(Long sessionId, String consultationText, Long review) {
        Optional<ConsultationSession> sessionOptional = consultationRepository.findById(sessionId);
        if (sessionOptional.isPresent()) {
            ConsultationSession session = sessionOptional.get();
            session.setConsultationText(consultationText);
            session.setReview(review);
            consultationRepository.save(session);
            return true;
        }
        return false;
    }


    private ConsultationDetailsDto mapToDto(ConsultationSession session) {
        return ConsultationDetailsDto.builder()
                .sessionId(session.getSessionId())
                .userId(session.getUserId())
                .managerId(session.getManagerId())
                .localDateTime(session.getLocalDateTime())
                .consultationDate(session.getConsultationDate())
                .consultationText(session.getConsultationText())
                .review(session.getReview())
                .build();
    }
}
