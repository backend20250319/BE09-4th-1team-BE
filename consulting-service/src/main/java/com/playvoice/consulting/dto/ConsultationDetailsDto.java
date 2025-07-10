package com.playvoice.consulting.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class ConsultationDetailsDto {
    private Long sessionId;

    @NotBlank(message = "사용자 ID는 필수이며 비어있을 수 없습니다.")
    private String userId;

    @NotBlank(message = "매니저 ID는 필수이며 비어있을 수 없습니다.")
    private String managerId;

    private LocalDate consultationDate;

    @NotNull
    private LocalDateTime localDateTime;
    private LocalDateTime reservationTime;
    private LocalDateTime cancelTime;
    private Status status;


    private String consultationText;

    private Long review;
}

