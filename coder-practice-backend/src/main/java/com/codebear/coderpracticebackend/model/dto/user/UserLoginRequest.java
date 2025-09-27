package com.codebear.coderpracticebackend.model.dto.user;

import lombok.Data;

@Data
public class UserLoginRequest {

    private String username;

    private String password;
}