package com.playvoice.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerCreateRequest {
    private String email;
    private String username;
    private String password;
    private String course;
} 