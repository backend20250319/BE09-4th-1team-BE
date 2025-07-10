package com.playvoice.userservice.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ManagerStatisticsDto {
    private Long managerId;
    private String username;
    private Long createdUserCount;
    private Long bannedUserCount;
    private Long passwordResetCount;
    private LocalDateTime lastActivityAt;
} 