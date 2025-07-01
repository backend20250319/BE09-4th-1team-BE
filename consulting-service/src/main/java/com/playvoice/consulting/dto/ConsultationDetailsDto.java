package com.playvoice.consulting.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class ConsultationDetailsDto {

    private Long sessionId;

    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String userId;

    @NotBlank(message = "매니저 ID는 필수입니다.")
    private String managerId;

    private LocalDate consultationDate;
    private LocalDateTime reservationTime;
    private LocalDateTime cancelTime;
    private Status status;

    private LocalDateTime localDateTime;
}
