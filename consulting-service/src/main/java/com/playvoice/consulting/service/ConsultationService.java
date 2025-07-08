package com.playvoice.consulting.service;

import com.playvoice.consulting.dto.ConsultationDetailsDto;
import com.playvoice.consulting.dto.Status;
import com.playvoice.consulting.enitiy.ConsultationSession;
import com.playvoice.consulting.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.playvoice.consulting.dto.ConsultationByUserDto;
import com.playvoice.consulting.dto.ConsultationByManagerDto;
import java.time.LocalDateTime;
import java.util.List;
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
            if (session.getStatus() != Status.상담완료) {
                return false;  // 상담이 완료되지 않았으면 저장하지 않음
            }
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
        session.setStatus(Status.예약취소);
        session.setCancelTime(LocalDateTime.now());

        consultationRepository.save(session);
        return mapToDto(session);
    }

    // 유저 ID 기준 조회 → managerId 출력
    @Transactional(readOnly = true)
    public List<ConsultationByUserDto> getConsultationsByUserId(String userId) {
        return consultationRepository.findByUserId(userId).stream()
                .map(session -> ConsultationByUserDto.builder()
                        .sessionId(session.getSessionId())
                        .managerId(session.getManagerId())
                        .consultationDate(session.getConsultationDate())
                        .localDateTime(session.getLocalDateTime())
                        .status(session.getStatus())
                        .build())
                .toList();
    }

    // 매니저 ID 기준 조회 → userId 출력
    @Transactional(readOnly = true)
    public List<ConsultationByManagerDto> getConsultationsByManagerId(String managerId) {
        return consultationRepository.findByManagerId(managerId).stream()
                .map(session -> ConsultationByManagerDto.builder()
                        .sessionId(session.getSessionId())
                        .userId(session.getUserId())
                        .consultationDate(session.getConsultationDate())
                        .localDateTime(session.getLocalDateTime())
                        .status(session.getStatus())
                        .build())
                .toList();
    }

    // 상태(status)로 조회
    @Transactional(readOnly = true)
    public List<ConsultationByUserDto> getConsultationsByUserIdAndStatus(String userId, Status status) {
        return consultationRepository.findByUserIdAndStatus(userId, status).stream()
                .map(session -> ConsultationByUserDto.builder()
                        .sessionId(session.getSessionId())
                        .managerId(session.getManagerId())
                        .consultationDate(session.getConsultationDate())
                        .localDateTime(session.getLocalDateTime())
                        .status(session.getStatus())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultationByManagerDto> getConsultationsByManagerIdAndStatus(String managerId, Status status) {
        return consultationRepository.findByManagerIdAndStatus(managerId, status).stream()
                .map(session -> ConsultationByManagerDto.builder()
                        .sessionId(session.getSessionId())
                        .userId(session.getUserId())
                        .consultationDate(session.getConsultationDate())
                        .localDateTime(session.getLocalDateTime())
                        .status(session.getStatus())
                        .build())
                .toList();
    }


    // 상태 업데이트
    @Transactional
    public ConsultationDetailsDto updateStatus(Long sessionId, Status newStatus) {
        Optional<ConsultationSession> optional = consultationRepository.findById(sessionId);
        if (optional.isEmpty()) return null;

        ConsultationSession session = optional.get();
        session.setStatus(newStatus);
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
