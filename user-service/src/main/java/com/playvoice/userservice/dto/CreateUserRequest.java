package com.playvoice.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String email;
    private String username;
    private String password;
    private String role;   // "STUDENT" 또는 "MANAGER"
    private String course;
} 