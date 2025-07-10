package com.playvoice.consulting.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusUpdateRequestDto {

    @NotNull(message = "변경할 상태값은 필수입니다.")
    private Status status;
}