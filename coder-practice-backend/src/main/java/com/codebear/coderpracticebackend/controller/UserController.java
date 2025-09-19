package com.codebear.coderpracticebackend.controller;

import com.codebear.coderpracticebackend.common.Result;
import com.codebear.coderpracticebackend.model.dto.user.UserLoginRequest;
import com.codebear.coderpracticebackend.model.dto.user.UserRegisterRequest;
import com.codebear.coderpracticebackend.model.vo.UserVO;
import com.codebear.coderpracticebackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<UserVO> register(@RequestBody UserRegisterRequest registerRequest) {
        UserVO userVO = userService.register(registerRequest);
        return Result.success(userVO);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody UserLoginRequest loginRequest) {
        UserVO userVO = userService.login(loginRequest);
        return Result.success(userVO);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/current")
    public Result<UserVO> getCurrentUser() {
        UserVO userVO = userService.getCurrentUser();
        return Result.success(userVO);
    }

    @Operation(summary = "用户注销")
    @PostMapping("/logout")
    public Result<String> logout() {
        userService.logout();
        return Result.success("注销成功");
    }
}