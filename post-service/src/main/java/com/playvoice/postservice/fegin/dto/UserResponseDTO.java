package com.playvoice.postservice.fegin.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(
    Long id,
    String email,
    String username,
    String name,
    UserRole role,    // MANAGER, STUDENT
    String course,
    Boolean isBanned,
    PasswordStatus passwordStatus,
    LocalDateTime createdAt,
    LocalDateTime lastChangedPassword,
    LocalDateTime lastLogin
) {

}
