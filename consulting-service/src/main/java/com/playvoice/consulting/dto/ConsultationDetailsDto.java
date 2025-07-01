package com.playvoice.consulting.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;

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
    //시간
}

