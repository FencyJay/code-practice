package com.codebear.coderpracticebackend.controller;

import com.codebear.coderpracticebackend.common.PageRequest;
import com.codebear.coderpracticebackend.common.PageResult;
import com.codebear.coderpracticebackend.common.Result;
import com.codebear.coderpracticebackend.model.dto.user.UserSubmitAnswerRequest;
import com.codebear.coderpracticebackend.model.entity.UserLevel;
import com.codebear.coderpracticebackend.service.UserLevelService;
import com.codebear.coderpracticebackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户闯关", description = "用户闯关相关接口")
@RestController
@RequestMapping("/user-level")
public class UserLevelController {

    @Autowired
    private UserLevelService userLevelService;

    @Autowired
    private UserService userService;

    @Operation(summary = "提交答案")
    @PostMapping("/submit")
    public Result<UserLevel> submitAnswer(@RequestBody UserSubmitAnswerRequest submitRequest) {
        Long userId = userService.getCurrentUser().getId();
        UserLevel userLevel = userLevelService.submitAnswer(userId, submitRequest);
        return Result.success(userLevel);
    }

    @Operation(summary = "获取用户闯关历史")
    @GetMapping("/history")
    public Result<PageResult<UserLevel>> getUserLevelHistory(PageRequest pageRequest) {
        Long userId = userService.getCurrentUser().getId();
        PageResult<UserLevel> result = userLevelService.getUserLevelHistory(userId, pageRequest);
        return Result.success(result);
    }

    @Operation(summary = "根据ID获取用户闯关结果详情")
    @GetMapping("/{id}")
    public Result<UserLevel> getUserLevelResult(@PathVariable("id") Long id) {
        Long currentUserId = userService.getCurrentUser().getId();
        UserLevel result = userLevelService.getUserLevelResult(id, currentUserId);
        return Result.success(result);
    }
}