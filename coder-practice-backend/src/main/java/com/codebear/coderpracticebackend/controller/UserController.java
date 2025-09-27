package com.codebear.coderpracticebackend.controller;

import com.codebear.coderpracticebackend.common.Result;
import com.codebear.coderpracticebackend.exception.BusinessException;
import com.codebear.coderpracticebackend.exception.ErrorCode;
import com.codebear.coderpracticebackend.model.dto.user.UserLoginRequest;
import com.codebear.coderpracticebackend.model.dto.user.UserRegisterRequest;
import com.codebear.coderpracticebackend.model.vo.UserVO;
import com.codebear.coderpracticebackend.model.vo.RankingUserVO;
import com.codebear.coderpracticebackend.service.UserService;
import com.codebear.coderpracticebackend.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<UserVO> register(@RequestBody UserRegisterRequest registerRequest) {
        // 参数校验
        ValidationUtils.notNull(registerRequest, "注册请求");
        ValidationUtils.notEmpty(registerRequest.getUsername(), "用户名");
        ValidationUtils.notEmpty(registerRequest.getPassword(), "密码");
        ValidationUtils.notEmpty(registerRequest.getConfirmPassword(), "确认密码");
        ValidationUtils.notEmpty(registerRequest.getNickname(), "昵称");

        // 校验用户名格式
        ValidationUtils.username(registerRequest.getUsername(), "用户名");

        // 校验密码格式
        ValidationUtils.password(registerRequest.getPassword(), "密码");

        // 校验昵称长度
        ValidationUtils.length(registerRequest.getNickname(), 2, 20, "昵称");

        // 校验两次密码是否一致
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }

        UserVO userVO = userService.register(registerRequest);
        return Result.success(userVO);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody UserLoginRequest loginRequest) {
        // 参数校验
        ValidationUtils.notNull(loginRequest, "登录请求");
        ValidationUtils.notEmpty(loginRequest.getUsername(), "用户名");
        ValidationUtils.notEmpty(loginRequest.getPassword(), "密码");

        // 校验用户名格式
        ValidationUtils.username(loginRequest.getUsername(), "用户名");

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

    @Operation(summary = "获取用户排行榜")
    @GetMapping("/ranking")
    public Result<List<RankingUserVO>> getUserRanking(
            @Parameter(description = "限制数量，范围1-100，默认30") @RequestParam(defaultValue = "30") Integer limit) {
        // 参数校验
        ValidationUtils.range(limit, 1, 100, "查询数量");

        List<RankingUserVO> rankingList = userService.getUserRanking(limit);
        return Result.success(rankingList);
    }
}