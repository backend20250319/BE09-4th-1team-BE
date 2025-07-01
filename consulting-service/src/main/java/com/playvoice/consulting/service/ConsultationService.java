package com.playvoice.consulting.service;

import com.playvoice.consulting.dto.ConsultationDetailsDto;
import com.playvoice.consulting.dto.Status;
import com.playvoice.consulting.enitiy.ConsultationSession;
import com.playvoice.consulting.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationService {

    private final ConsultationRepository consultationRepository;

    @Transactional(readOnly = true)
    public ConsultationDetailsDto getConsultationDetails(Long sessionId) {
        return consultationRepository.findById(sessionId)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Transactional
    public Long createNewSession(String userId, String managerId, LocalDateTime dateTime) {
        ConsultationSession newSession = new ConsultationSession();
        newSession.setUserId(userId);
        newSession.setManagerId(managerId);
        newSession.setLocalDateTime(dateTime);
        newSession.setConsultationDate(dateTime.toLocalDate());
        newSession.setReservationTime(LocalDateTime.now());
        newSession.setStatus(Status.예약대기);

        ConsultationSession saved = consultationRepository.save(newSession);
        return saved.getSessionId();
    }

    // ✅ 예약 취소 메서드
    @Transactional
    public ConsultationDetailsDto cancelConsultation(Long sessionId) {
        Optional<ConsultationSession> optional = consultationRepository.findById(sessionId);
        if (optional.isEmpty()) return null;

        ConsultationSession session = optional.get();
        session.setStatus(Status.예약취소);
        session.setCancelTime(LocalDateTime.now());

        consultationRepository.save(session);

        return mapToDto(session);
    }

    private ConsultationDetailsDto mapToDto(ConsultationSession session) {
        return ConsultationDetailsDto.builder()
                .sessionId(session.getSessionId())
                .userId(session.getUserId())
                .managerId(session.getManagerId())
                .consultationDate(session.getConsultationDate())
                .reservationTime(session.getReservationTime())
                .cancelTime(session.getCancelTime())
                .status(session.getStatus())
                .localDateTime(session.getLocalDateTime())
                .build();
    }
}
