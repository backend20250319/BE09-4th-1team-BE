package com.playvoice.consulting.controller;

import com.playvoice.consulting.dto.ConsultationDetailsDto;
import com.playvoice.consulting.dto.ConsultationFeedbackDto;
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

    // 1. 예약 생성 API
    @PostMapping
    public ResponseEntity<ResponseMessage> insertConsultation(@Valid @RequestBody ConsultationDetailsDto consultationDetailsDto) {
        log.info("수신된 상담 예약 요청: {}", consultationDetailsDto);
        try {
            Long sessionId = consultationService.createNewSession(
                    consultationDetailsDto.getUserId(),
                    consultationDetailsDto.getManagerId(),
                    consultationDetailsDto.getLocalDateTime()
                    // consultationText와 review는 이제 여기서 받지 않습니다.
            );

            Map<String, Object> results = new HashMap<>();
            results.put("sessionId", sessionId);

            ResponseMessage responseMessage = new ResponseMessage(
                    "상담 예약이 성공적으로 생성되었습니다.",
                    results
            );

            log.info("상담 예약 생성 완료. 세션 ID: {}", sessionId);
            return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("상담 예약 생성 중 오류 발생: {}", e.getMessage(), e);
            ResponseMessage errorResponse = new ResponseMessage(
                    "상담 예약 생성에 실패했습니다. 서버 내부 오류.",
                    null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
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

    // 3. 피드백 및 평점 작성 API (새로운 엔드포인트)
    @PostMapping("/{sessionId}/feedback") // 예: POST /api/v1/consultation/{sessionId}/feedback
    public ResponseEntity<ResponseMessage> submitFeedbackAndReview(
            @PathVariable Long sessionId,
            @Valid @RequestBody ConsultationFeedbackDto feedbackDto) {
        log.info("세션 ID {}에 대한 피드백 및 평점 요청 수신: {}", sessionId, feedbackDto);
        try {
            boolean success = consultationService.submitFeedbackAndReview(
                    sessionId,
                    feedbackDto.getConsultationText(),
                    feedbackDto.getReview()
            );

            if (success) {
                ResponseMessage responseMessage = new ResponseMessage(
                        "피드백 및 평점이 성공적으로 저장되었습니다.",
                        null // 결과 데이터는 필요 없을 수 있습니다.
                );
                log.info("세션 ID {}에 대한 피드백 및 평점 저장 완료.", sessionId);
                return new ResponseEntity<>(responseMessage, HttpStatus.OK);
            } else {
                ResponseMessage notFoundResponse = new ResponseMessage(
                        "ID " + sessionId + "에 해당하는 상담 세션을 찾을 수 없어 피드백을 저장할 수 없습니다.",
                        null
                );
                log.warn("세션 ID {}에 대한 피드백 저장 실패: 세션 없음.", sessionId);
                return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("세션 ID {}에 대한 피드백 및 평점 저장 중 오류 발생: {}", sessionId, e.getMessage(), e);
            ResponseMessage errorResponse = new ResponseMessage(
                    "피드백 및 평점 저장 중 오류가 발생했습니다. 서버 내부 오류.",
                    null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 유효성 검사 실패 시 처리하는 전역 핸들러
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
