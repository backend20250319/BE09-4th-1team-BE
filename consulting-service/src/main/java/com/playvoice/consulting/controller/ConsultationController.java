package com.playvoice.consulting.controller;


import com.playvoice.consulting.dto.ConsultationDetailsDto;
import com.playvoice.consulting.dto.ResponseMessage;
import com.playvoice.consulting.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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
                    consultationDetailsDto.getLocalDateTime(),
                    consultationDetailsDto.getConsultationText(),
                    consultationDetailsDto.getReview()
            );

            Map<String, Object> results = new HashMap<>();
            results.put("sessionId", sessionId);

            ResponseMessage responseMessage = new ResponseMessage(
                    "상담 세션이 성공적으로 생성되었습니다.",
                    results
            );

            log.info("상담 세션 ID: {}로 생성되었습니다.", sessionId);
            return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("상담 세션 생성 중 오류 발생: {}", e.getMessage(), e);
            ResponseMessage errorResponse = new ResponseMessage(
                    "상담 세션 생성에 실패했습니다. 서버 내부 오류.",
                    null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{sessionId}")
    public ResponseEntity<ResponseMessage> getConsultationDetails(@PathVariable Long sessionId) {
        log.info("세션 ID {}에 대한 상담 세부 정보를 가져오는 중입니다.", sessionId);
        try {
            ConsultationDetailsDto details = consultationService.getConsultationDetails(sessionId);

            if (details != null) {
                Map<String, Object> results = new HashMap<>();
                results.put("consultationDetails", details);

                ResponseMessage responseMessage = new ResponseMessage(
                        "상담 세부 정보가 성공적으로 조회되었습니다.",
                        results
                );
                return new ResponseEntity<>(responseMessage, HttpStatus.OK);
            } else {
                ResponseMessage notFoundResponse = new ResponseMessage(
                        "ID " + sessionId + "에 대한 상담 세션이 발견되지 않았습니다.",
                        null
                );
                return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("세션 ID {}에 대한 상담 세부 정보 조회 중 오류 발생: {}", sessionId, e.getMessage(), e);
            ResponseMessage errorResponse = new ResponseMessage(
                    "상담 세부 정보 조회에 실패했습니다. 서버 내부 오류.",
                    null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()
                ));

        ResponseMessage errorResponse = new ResponseMessage(
                "요청 데이터 유효성 검사 실패",
                errors
        );
        log.warn("유효성 검사 실패: {}", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

