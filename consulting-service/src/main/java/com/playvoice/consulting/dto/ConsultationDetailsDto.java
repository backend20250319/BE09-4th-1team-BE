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
    //시간
    @NotBlank(message = "상담 피드백은 필수이며 비어있을 수 없습니다.")
    @Size(min = 10, message = "상담 피드백은 최소 10자 이상으로 작성해야 합니다.")
    private String consultationText;

    @NotNull(message = "별점을 남겨주세요.")
    @Min(value=1,message = "최소 1점까지만 입니다.")
    @Max(value=5,message = "최대 5까지 입니다.")
    private Long review;
}

