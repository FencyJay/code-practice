package com.codebear.coderpracticebackend.controller;

import com.codebear.coderpracticebackend.common.PageRequest;
import com.codebear.coderpracticebackend.common.PageResult;
import com.codebear.coderpracticebackend.common.Result;
import com.codebear.coderpracticebackend.exception.BusinessException;
import com.codebear.coderpracticebackend.exception.ErrorCode;
import com.codebear.coderpracticebackend.model.entity.Level;
import com.codebear.coderpracticebackend.model.dto.level.LevelQueryRequest;
import com.codebear.coderpracticebackend.service.LevelService;
import com.codebear.coderpracticebackend.service.UserService;
import com.codebear.coderpracticebackend.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Tag(name = "关卡管理", description = "关卡相关接口")
@RestController
@RequestMapping("/level")
public class LevelController {

    @Autowired
    private LevelService levelService;

    @Autowired
    private UserService userService;

    @Operation(summary = "获取关卡列表")
    @GetMapping("/list")
    public Result<PageResult<Level>> getLevelList(PageRequest pageRequest,
                                                   @RequestParam(required = false) String difficulty,
                                                   @RequestParam(required = false) Boolean isFeatured) {
        // 参数校验
        ValidationUtils.notNull(pageRequest, "分页请求");
        if (!pageRequest.isValid()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分页参数无效");
        }

        // 难度参数校验
        String[] allowedDifficulties = {"简单", "中等", "困难"};
        ValidationUtils.enumValue(difficulty, allowedDifficulties, "难度");

        // 构建查询请求对象
        LevelQueryRequest levelQueryRequest = new LevelQueryRequest();
        levelQueryRequest.setCurrent(pageRequest.getCurrent());
        levelQueryRequest.setPageSize(pageRequest.getPageSize());
        levelQueryRequest.setDifficulty(difficulty);
        levelQueryRequest.setIsFeatured(isFeatured);

        PageResult<Level> result = levelService.getLevelList(levelQueryRequest);
        return Result.success(result);
    }

    @Operation(summary = "根据ID获取关卡详情")
    @GetMapping("/{id}")
    public Result<Level> getLevelById(@Parameter(description = "关卡ID") @PathVariable Long id) {
        // 参数校验
        ValidationUtils.positiveId(id, "关卡ID");

        Level level = levelService.getLevelById(id);
        return Result.success(level);
    }

    @Operation(summary = "生成关卡")
    @PostMapping("/generate")
    public Result<Level> generateLevel(@Parameter(description = "用户薪资") @RequestParam Integer salary,
                                      @Parameter(description = "岗位方向") @RequestParam(required = false) String direction) {
        // 参数校验
        ValidationUtils.salary(salary, "薪资");



        userService.getCurrentUser();
        Level level = levelService.generateLevel(salary, direction);
        return Result.success(level);
    }
}