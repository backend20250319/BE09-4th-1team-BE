package com.playvoice.userservice.dto;

import com.playvoice.userservice.entity.User;
import com.playvoice.userservice.entity.UserRole;
import lombok.Getter;

@Getter
public class UserSimpleInfoDto {
    private final Long userId;
    private final String course;
    private final UserRole role;
    private final String name;

    public UserSimpleInfoDto(User user) {
        this.userId = user.getId();
        this.course = user.getCourse();
        this.role = user.getRole();
        this.name = user.getName();
    }
} 