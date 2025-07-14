package com.playvoice.consulting.controller;

import com.playvoice.consulting.dto.*;
import com.playvoice.consulting.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/consulting")
@RequiredArgsConstructor
@Slf4j
public class ConsultationController {

    private final ConsultationService consultationService;

    // 1. 상담 예약 생성 API
    @PostMapping
    public ResponseEntity<ResponseMessage> insertConsultation(@Valid @RequestBody ConsultationDetailsDto consultationDetailsDto) {
        log.info("수신된 상담 예약 요청: {}", consultationDetailsDto);
        try {
            Long sessionId = consultationService.createNewSession(
                    consultationDetailsDto.getUserId(),
                    consultationDetailsDto.getManagerId(),
                    consultationDetailsDto.getLocalDateTime()
            );

            Map<String, Object> results = new HashMap<>();
            results.put("sessionId", sessionId);

            return new ResponseEntity<>(
                    new ResponseMessage("상담 예약이 성공적으로 생성되었습니다.", results),
                    HttpStatus.CREATED
            );

        } catch (Exception e) {
            log.error("상담 예약 생성 중 오류 발생: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseMessage("상담 예약 생성에 실패했습니다. 서버 내부 오류.", null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // 2. 상담 세부 정보 조회 API
    @GetMapping("/{sessionId}")
    public ResponseEntity<ResponseMessage> getConsultationDetails(@PathVariable Long sessionId) {
        log.info("세션 ID {}에 대한 상담 세부 정보를 가져오는 중입니다.", sessionId);
        try {
            ConsultationDetailsDto details = consultationService.getConsultationDetails(sessionId);

            if (details != null) {
                Map<String, Object> results = new HashMap<>();
                results.put("consultationDetails", details);
                return new ResponseEntity<>(
                        new ResponseMessage("상담 세부 정보가 성공적으로 조회되었습니다.", results),
                        HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(
                        new ResponseMessage("ID " + sessionId + "에 대한 상담 세션이 발견되지 않았습니다.", null),
                        HttpStatus.NOT_FOUND
                );
            }
        } catch (Exception e) {
            log.error("상담 세부 정보 조회 중 오류 발생: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseMessage("상담 세부 정보 조회에 실패했습니다. 서버 내부 오류.", null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // 유저 ID 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseMessage> getConsultationsByUserId(@PathVariable String userId) {
        try {
            List<ConsultationByUserDto> consultations = consultationService.getConsultationsByUserId(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("consultations", consultations);

            return ResponseEntity.ok(new ResponseMessage("유저의 상담 예약 목록을 성공적으로 조회했습니다.", result));
        } catch (Exception e) {
            log.error("유저 예약 조회 중 오류 발생: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ResponseMessage("유저 상담 조회 실패", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 매니저 ID 조회
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<ResponseMessage> getConsultationsByManagerId(@PathVariable String managerId) {
        try {
            List<ConsultationByManagerDto> consultations = consultationService.getConsultationsByManagerId(managerId);
            Map<String, Object> result = new HashMap<>();
            result.put("consultations", consultations);

            return ResponseEntity.ok(new ResponseMessage("매니저의 상담 예약 목록을 성공적으로 조회했습니다.", result));
        } catch (Exception e) {
            log.error("매니저 예약 조회 중 오류 발생: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ResponseMessage("매니저 상담 조회 실패", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //상태별 조회
    @GetMapping("/user/{userId}/status")
    public ResponseEntity<ResponseMessage> getConsultationsByUserIdAndStatus(
            @PathVariable String userId,
            @RequestParam("status") Status status) {
        try {
            List<ConsultationByUserDto> consultations = consultationService.getConsultationsByUserIdAndStatus(userId, status);
            Map<String, Object> result = new HashMap<>();
            result.put("consultations", consultations);

            return ResponseEntity.ok(new ResponseMessage("유저 ID 및 상태로 조회 성공", result));
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessage("유저 상담 조회 중 오류", null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/manager/{managerId}/status")
    public ResponseEntity<ResponseMessage> getConsultationsByManagerIdAndStatus(
            @PathVariable String managerId,
            @RequestParam("status") Status status) {
        try {
            List<ConsultationByManagerDto> consultations = consultationService.getConsultationsByManagerIdAndStatus(managerId, status);
            Map<String, Object> result = new HashMap<>();
            result.put("consultations", consultations);

            return ResponseEntity.ok(new ResponseMessage("매니저 ID 및 상태로 조회 성공", result));
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessage("매니저 상담 조회 중 오류", null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // 3. 피드백 및 평점 작성 API

    @PostMapping("/{sessionId}/feedback")
    public ResponseEntity<ResponseMessage> submitFeedbackAndReview(
            @PathVariable Long sessionId,
            @Valid @RequestBody ConsultationFeedbackDto feedbackDto) {
        log.info("세션 ID {}에 대한 피드백 요청 수신: {}", sessionId, feedbackDto);
        try {
            boolean success = consultationService.submitFeedbackAndReview(
                    sessionId,
                    feedbackDto.getConsultationText(),
                    feedbackDto.getReview()
            );

            if (success) {
                return new ResponseEntity<>(
                        new ResponseMessage("피드백 및 평점이 성공적으로 저장되었습니다.", null),
                        HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(
                        new ResponseMessage("상담완료 상태에서만 리뷰를 작성할 수 있습니다.", null),
                        HttpStatus.BAD_REQUEST
                );
            }
        } catch (Exception e) {
            log.error("피드백 저장 중 오류 발생: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseMessage("피드백 저장 중 서버 오류.", null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // 4. 상담 취소 API (예약취소 상태로 반영)
    @PatchMapping("/{sessionId}/cancel")
    public ResponseEntity<ResponseMessage> cancelConsultation(@PathVariable Long sessionId) {
        try {
            ConsultationDetailsDto updated = consultationService.cancelConsultation(sessionId);

            if (updated == null) {
                return new ResponseEntity<>(
                        new ResponseMessage("해당 ID의 상담 세션이 존재하지 않습니다.", null),
                        HttpStatus.NOT_FOUND
                );
            }

            Map<String, Object> results = new HashMap<>();
            results.put("sessionId", updated.getSessionId());
            results.put("cancelTime", updated.getCancelTime());
            results.put("status", updated.getStatus());

            return new ResponseEntity<>(
                    new ResponseMessage( "상담 예약이 성공적으로 취소되었습니다.", results),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.error("예약 취소 중 오류 발생: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseMessage("예약 취소 중 서버 오류", null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping("/{sessionId}/status")
    public ResponseEntity<ResponseMessage> updateConsultationStatus(
            @PathVariable Long sessionId,
            @Valid @RequestBody StatusUpdateRequestDto requestDto) {
        try {
            ConsultationDetailsDto updated = consultationService.updateStatus(sessionId, requestDto.getStatus());

            if (updated == null) {
                return new ResponseEntity<>(
                        new ResponseMessage("해당 ID의 상담 세션이 존재하지 않습니다.", null),
                        HttpStatus.NOT_FOUND
                );
            }

            Map<String, Object> result = new HashMap<>();
            result.put("sessionId", updated.getSessionId());
            result.put("status", updated.getStatus());

            return new ResponseEntity<>(
                    new ResponseMessage("상담 세션 상태가 성공적으로 변경되었습니다.", result),
                    HttpStatus.OK
            );

        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessage("상담 세션 상태 변경 중 오류가 발생했습니다.", null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // 유효성 검증 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()
                ));

        return new ResponseEntity<>(
                new ResponseMessage("요청 데이터 유효성 검사 실패", errors),
                HttpStatus.BAD_REQUEST
        );
    }
}
