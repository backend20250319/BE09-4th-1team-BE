package com.playvoice.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerUpdateRequest {
    private String email;
    private String name;
    private String course;
} 