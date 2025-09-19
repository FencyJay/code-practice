package com.codebear.coderpracticebackend.controller;

import com.codebear.coderpracticebackend.common.PageRequest;
import com.codebear.coderpracticebackend.common.PageResult;
import com.codebear.coderpracticebackend.common.Result;
import com.codebear.coderpracticebackend.model.entity.Level;
import com.codebear.coderpracticebackend.service.LevelService;
import com.codebear.coderpracticebackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "关卡管理", description = "关卡相关接口")
@RestController
@RequestMapping("/api/level")
public class LevelController {

    @Autowired
    private LevelService levelService;

    @Autowired
    private UserService userService;

    @Operation(summary = "获取关卡列表")
    @GetMapping("/list")
    public Result<PageResult<Level>> getLevelList(PageRequest pageRequest,
                                                   @RequestParam(required = false) String difficulty) {
        PageResult<Level> result = levelService.getLevelList(pageRequest, difficulty);
        return Result.success(result);
    }

    @Operation(summary = "根据ID获取关卡详情")
    @GetMapping("/{id}")
    public Result<Level> getLevelById(@PathVariable Long id) {
        Level level = levelService.getLevelById(id);
        return Result.success(level);
    }

    @Operation(summary = "生成关卡")
    @PostMapping("/generate")
    public Result<Level> generateLevel(@RequestParam Integer salary) {
        userService.getCurrentUser();
        Level level = levelService.generateLevel(salary);
        return Result.success(level);
    }
}