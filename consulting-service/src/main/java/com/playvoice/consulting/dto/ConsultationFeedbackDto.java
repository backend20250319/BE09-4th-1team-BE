package com.playvoice.consulting.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ConsultationFeedbackDto {

    @NotBlank(message = "상담 피드백은 필수이며 비어있을 수 없습니다.")
    @Size(min = 10, message = "상담 피드백은 최소 10자 이상으로 작성해야 합니다.")
    private String consultationText;

    @NotNull(message = "별점을 남겨주세요.")
    @Min(value = 1, message = "별점은 최소 1점입니다.")
    @Max(value = 5, message = "별점은 최대 5점입니다.")
    private Long review;
}