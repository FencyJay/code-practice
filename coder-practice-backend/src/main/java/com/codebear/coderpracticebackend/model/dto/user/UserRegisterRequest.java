package com.codebear.coderpracticebackend.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    private String nickname;
}