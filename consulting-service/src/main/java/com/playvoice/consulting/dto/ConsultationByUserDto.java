package com.playvoice.consulting.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ConsultationByUserDto {
    private Long sessionId;
    private String managerId;
    private LocalDate consultationDate;
    private LocalDateTime localDateTime;
    private Status status;
}
