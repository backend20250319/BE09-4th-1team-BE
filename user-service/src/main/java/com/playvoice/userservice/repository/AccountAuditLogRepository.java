package com.playvoice.userservice.repository;

import com.playvoice.userservice.entity.AccountAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AccountAuditLogRepository extends JpaRepository<AccountAuditLog, Long> {
    List<AccountAuditLog> findByPerformedBy(Long managerId);
    List<AccountAuditLog> findByPerformedByAndAction(Long managerId, String action);
    Optional<AccountAuditLog> findTopByPerformedByOrderByTimestampDesc(Long managerId);
} 