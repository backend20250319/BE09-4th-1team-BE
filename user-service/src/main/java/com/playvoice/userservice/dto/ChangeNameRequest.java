package com.playvoice.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeNameRequest {
    private String password;
    private String newName;
}
