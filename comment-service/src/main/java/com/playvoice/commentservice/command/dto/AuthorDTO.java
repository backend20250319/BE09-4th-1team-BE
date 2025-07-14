package com.playvoice.commentservice.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthorDTO {

    @Schema(description = "작성자 ID", example = "7")
    private Long userId;

    @Schema(description = "작성자 이름", example = "정아")
    private String name;
}
