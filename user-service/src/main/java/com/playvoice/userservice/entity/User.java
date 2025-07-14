package com.playvoice.userservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String name;

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

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
        this.passwordStatus = PasswordStatus.CHANGED;
        this.lastChangedPassword = LocalDateTime.now();
    }

    public void changeEmail(String newEmail) {
        this.email = newEmail;
    }

    public void changeName(String newName) {
        this.name = newName;
    }

    public void setPasswordStatus(PasswordStatus passwordStatus) {
        this.passwordStatus = passwordStatus;
    }

    public void setIsBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }
}
