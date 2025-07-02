package com.playvoice.consulting.service;

import com.playvoice.consulting.dto.ConsultationDetailsDto;
import com.playvoice.consulting.dto.Status;
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

    // 1. 상담 세부 조회
    @Transactional(readOnly = true)
    public ConsultationDetailsDto getConsultationDetails(Long sessionId) {
        Optional<ConsultationSession> sessionOptional = consultationRepository.findById(sessionId);
        return sessionOptional.map(this::mapToDto).orElse(null);
    }

    // 2. 예약 생성
    @Transactional
    public Long createNewSession(String userId, String managerId, LocalDateTime dateTime) {
        ConsultationSession newSession = new ConsultationSession();
        newSession.setUserId(userId);
        newSession.setManagerId(managerId);
        newSession.setLocalDateTime(dateTime);
        newSession.setConsultationDate(dateTime.toLocalDate());
        newSession.setReservationTime(LocalDateTime.now());
        newSession.setStatus(Status.예약대기); // 기본 상태 설정
        consultationRepository.save(newSession);
        return newSession.getSessionId();
    }

    // 3. 피드백 및 리뷰 저장
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

    // 4. 예약 취소
    @Transactional
    public ConsultationDetailsDto cancelConsultation(Long sessionId) {
        Optional<ConsultationSession> sessionOptional = consultationRepository.findById(sessionId);
        if (sessionOptional.isEmpty()) {
            return null;
        }

        ConsultationSession session = sessionOptional.get();
        session.setStatus(Status.예약취소); // 예약취소 상태로 변경
        session.setCancelTime(LocalDateTime.now());

        consultationRepository.save(session);

        return mapToDto(session);
    }

    // DTO 매핑
    private ConsultationDetailsDto mapToDto(ConsultationSession session) {
        return ConsultationDetailsDto.builder()
                .sessionId(session.getSessionId())
                .userId(session.getUserId())
                .managerId(session.getManagerId())
                .localDateTime(session.getLocalDateTime())
                .consultationDate(session.getConsultationDate())
                .reservationTime(session.getReservationTime())
                .cancelTime(session.getCancelTime())
                .consultationText(session.getConsultationText())
                .review(session.getReview())
                .status(session.getStatus())
                .build();
    }
}
