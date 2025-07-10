package com.playvoice.userservice.service;

import com.playvoice.userservice.entity.AccountAuditLog;
import com.playvoice.userservice.entity.AuditAction;
import com.playvoice.userservice.repository.AccountAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountAuditLogService {
    private final AccountAuditLogRepository auditLogRepository;

    public void log(Long targetUserId, Long performedBy, AuditAction action) {
        auditLogRepository.save(AccountAuditLog.builder()
            .targetUserId(targetUserId)
            .performedBy(performedBy)
            .action(action.name())
            .timestamp(LocalDateTime.now())
            .build());
    }
} 