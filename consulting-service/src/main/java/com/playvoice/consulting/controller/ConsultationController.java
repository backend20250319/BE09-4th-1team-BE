package com.playvoice.consulting.controller;

import com.playvoice.consulting.dto.ConsultationDetailsDto;
import com.playvoice.consulting.dto.ResponseMessage;
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

@RestController
@RequestMapping("/api/v1/consultation")
    @RequiredArgsConstructor
    @Slf4j
    public class ConsultationController {

        private final ConsultationService consultationService;

    // 1. 예약하기 (상담 삽입)
    @PostMapping
    public ResponseEntity<ResponseMessage> insertConsultation(@Valid @RequestBody ConsultationDetailsDto consultationDetailsDto) {
        log.info("수신된 상담 요청: {}", consultationDetailsDto);
        try {
            Long sessionId = consultationService.createNewSession(
                    consultationDetailsDto.getUserId(),
                    consultationDetailsDto.getManagerId(),
                    consultationDetailsDto.getLocalDateTime()
            );

            Map<String, Object> results = new HashMap<>();
            results.put("sessionId", sessionId);

            ResponseMessage responseMessage = new ResponseMessage(
                    HttpStatus.CREATED.value(),
                    "상담 세션이 성공적으로 생성되었습니다.",
                    results
            );

            log.info("상담 세션 ID: {}로 생성되었습니다.", sessionId);
            return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("상담 세션 생성 중 오류 발생: {}", e.getMessage(), e);
            ResponseMessage errorResponse = new ResponseMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "상담 세션 생성에 실패했습니다. 서버 내부 오류.", // 좀 더 명확한 메시지
                    null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. 상담 세부 정보 조회
    @GetMapping("/{sessionId}")
    public ResponseEntity<ResponseMessage> getConsultationDetails(@PathVariable Long sessionId) {
        log.info("세션 ID {}에 대한 상담 세부 정보를 가져오는 중입니다.", sessionId);
        try {
            ConsultationDetailsDto details = consultationService.getConsultationDetails(sessionId);

            if (details != null) {
                Map<String, Object> results = new HashMap<>();
                results.put("sessionId", details.getSessionId());
                results.put("userId", details.getUserId());
                results.put("managerId", details.getManagerId());
                results.put("consultationDate", details.getConsultationDate());
                results.put("reservationTime", details.getReservationTime());
                results.put("cancelTime", details.getCancelTime());
                results.put("status", details.getStatus());
                results.put("localDateTime", details.getLocalDateTime());

                ResponseMessage responseMessage = new ResponseMessage(
                        HttpStatus.OK.value(),
                        "상담 세부 정보가 성공적으로 조회되었습니다.",
                        results
                );
                return new ResponseEntity<>(responseMessage, HttpStatus.OK);
            } else {
                ResponseMessage notFoundResponse = new ResponseMessage(
                        HttpStatus.NOT_FOUND.value(),
                        "ID " + sessionId + "에 대한 상담 세션이 발견되지 않았습니다.",
                        null
                );
                return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("세션 ID {}에 대한 상담 세부 정보 조회 중 오류 발생: {}", sessionId, e.getMessage(), e);
            ResponseMessage errorResponse = new ResponseMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "상담 세부 정보 조회에 실패했습니다. 서버 내부 오류.",
                    null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{sessionId}/cancel")
    public ResponseEntity<ResponseMessage> cancelConsultation(@PathVariable Long sessionId) {
        try {
            ConsultationDetailsDto updated = consultationService.cancelConsultation(sessionId);

            if (updated == null) {
                return new ResponseEntity<>(new ResponseMessage(
                        404, "해당 ID의 상담 세션이 존재하지 않습니다.", null),
                        HttpStatus.NOT_FOUND);
            }

            Map<String, Object> results = new HashMap<>();
            results.put("sessionId", updated.getSessionId());
            results.put("cancelTime", updated.getCancelTime());
            results.put("status", updated.getStatus());

            return new ResponseEntity<>(new ResponseMessage(
                    200, "상담 예약이 성공적으로 취소되었습니다.", results
            ), HttpStatus.OK);
        } catch (Exception e) {
            log.error("예약 취소 중 오류 발생: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ResponseMessage(
                    500, "예약 취소 중 서버 오류", null
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(), // 오류가 발생한 필드 이름
                        fieldError -> fieldError.getDefaultMessage() // 해당 필드의 오류 메시지
                ));

        ResponseMessage errorResponse = new ResponseMessage(
                HttpStatus.BAD_REQUEST.value(), 
                "요청 데이터 유효성 검사 실패",
                errors
        );
        log.warn("유효성 검사 실패: {}", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}