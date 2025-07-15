package com.playvoice.userservice.dto;

import com.playvoice.userservice.entity.PasswordStatus;
import com.playvoice.userservice.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String username;
    private UserRole role;
    private PasswordStatus passwordStatus;
    private String course;
}
