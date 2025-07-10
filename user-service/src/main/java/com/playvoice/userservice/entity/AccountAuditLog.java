package com.playvoice.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_audit_log")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long targetUserId;
    private String action;
    private Long performedBy;
    private LocalDateTime timestamp;
} 