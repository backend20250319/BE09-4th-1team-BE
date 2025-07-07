package com.playvoice.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_tbl")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // 외부에서 New 막기
@AllArgsConstructor(access = AccessLevel.PRIVATE)       // builder 사용 유도
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;      // MANAGER, STUDENT

    @Column(nullable = false)
    private String course;

    @Column(nullable = false)
    private Boolean isBanned;

    @Column(nullable = false)
    private PasswordStatus passwordStatus;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastChangedPassword;

    @Column(nullable = false)
    private LocalDateTime lastLogin;
}
