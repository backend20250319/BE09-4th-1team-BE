package com.playvoice.userservice.controller;

import com.playvoice.userservice.dto.CreateUserRequest;
import com.playvoice.userservice.dto.ManagerCreateRequest;
import com.playvoice.userservice.dto.ManagerUpdateRequest;
import com.playvoice.userservice.dto.ManagerStatisticsDto;
import com.playvoice.userservice.entity.AuditAction;
import com.playvoice.userservice.entity.User;
import com.playvoice.userservice.repository.AccountAuditLogRepository;
import com.playvoice.userservice.service.AccountAuditLogService;
import com.playvoice.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {
    private final UserService userService;
    private final AccountAuditLogService auditLogService;
    private final AccountAuditLogRepository auditLogRepository;

    @PostMapping
    public ResponseEntity<User> createManager(@RequestBody ManagerCreateRequest req, @AuthenticationPrincipal String managerId) {
        User manager = userService.createManager(req);
        auditLogService.log(manager.getId(), Long.valueOf(managerId), AuditAction.CREATE_MANAGER);
        return ResponseEntity.ok(manager);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request, @AuthenticationPrincipal String managerId) {
        User user = userService.createUser(request);
        auditLogService.log(user.getId(), Long.valueOf(managerId), AuditAction.CREATE_USER);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/users/{id}/ban")
    public ResponseEntity<Void> banUser(@PathVariable Long id, @AuthenticationPrincipal String managerId) {
        userService.banStudent(id);
        auditLogService.log(id, Long.valueOf(managerId), AuditAction.BAN_USER);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/users/{id}/unban")
    public ResponseEntity<Void> unbanUser(@PathVariable Long id, @AuthenticationPrincipal String managerId) {
        userService.unbanStudent(id);
        auditLogService.log(id, Long.valueOf(managerId), AuditAction.UNBAN_USER);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/reset-password")
    public ResponseEntity<Void> resetUserPassword(@PathVariable Long id, @AuthenticationPrincipal String managerId) {
        userService.resetStudentPassword(id);
        auditLogService.log(id, Long.valueOf(managerId), AuditAction.PASSWORD_RESET);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getManagers(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(userService.getManagers(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getManager(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getManager(id));
    }

    @GetMapping("/statistics/{managerId}")
    public ResponseEntity<ManagerStatisticsDto> getManagerStatistics(@PathVariable Long managerId) {
        // managerId로 username 조회
        User manager = userService.getManager(managerId);
        var logs = auditLogRepository.findByPerformedBy(managerId);
        long createdUserCount = logs.stream().filter(l -> l.getAction().equals("CREATE_USER")).count();
        long bannedUserCount = logs.stream().filter(l -> l.getAction().equals("BAN_USER")).count();
        long passwordResetCount = logs.stream().filter(l -> l.getAction().equals("PASSWORD_RESET")).count();
        var lastActivityAt = logs.stream().map(l -> l.getTimestamp()).max(java.time.LocalDateTime::compareTo).orElse(null);
        return ResponseEntity.ok(ManagerStatisticsDto.builder()
            .managerId(managerId)
            .username(manager.getUsername())
            .createdUserCount(createdUserCount)
            .bannedUserCount(bannedUserCount)
            .passwordResetCount(passwordResetCount)
            .lastActivityAt(lastActivityAt)
            .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateManager(@PathVariable Long id, @RequestBody ManagerUpdateRequest req, @AuthenticationPrincipal String managerId) {
        User updated = userService.updateManager(id, req);
        auditLogService.log(id, Long.valueOf(managerId), AuditAction.UPDATE_MANAGER);
        return ResponseEntity.ok(updated);
    }
}
