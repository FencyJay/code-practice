package com.codebear.coderpracticebackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codebear.coderpracticebackend.common.PageRequest;
import com.codebear.coderpracticebackend.common.Result;
import com.codebear.coderpracticebackend.model.entity.Level;
import com.codebear.coderpracticebackend.model.entity.User;
import com.codebear.coderpracticebackend.model.dto.level.LevelAddRequest;
import com.codebear.coderpracticebackend.model.dto.level.LevelQueryRequest;
import com.codebear.coderpracticebackend.model.dto.level.LevelUpdateRequest;
import com.codebear.coderpracticebackend.model.dto.level.SetFeaturedRequest;
import com.codebear.coderpracticebackend.service.LevelService;
import com.codebear.coderpracticebackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员控制器
 *
 * @author fency
 */
@RestController
@RequestMapping("/admin")
@Tag(name = "管理员管理", description = "管理员相关接口")
public class AdminController {

    @Resource
    private LevelService levelService;

    @Resource
    private UserService userService;

    /**
     * 分页获取关卡列表（按优先级排序）
     */
    @Operation(summary = "分页获取关卡列表")
    @GetMapping("/levels")
    public Result<Page<Level>> getLevelsByPage(LevelQueryRequest levelQueryRequest) {
        Page<Level> levelPage = levelService.getLevelsByPage(levelQueryRequest);
        return Result.success(levelPage);
    }

    /**
     * 获取所有关卡（按优先级排序）- 保持兼容性
     */
    @Operation(summary = "获取所有关卡")
    @GetMapping("/levels/all")
    public Result<List<Level>> getAllLevels() {
        List<Level> levels = levelService.getAllLevelsOrderByPriority();
        return Result.success(levels);
    }

    /**
     * 添加关卡
     */
    @Operation(summary = "添加关卡")
    @PostMapping("/levels")
    public Result<Long> addLevel(@RequestBody LevelAddRequest levelAddRequest) {
        Long levelId = levelService.addLevel(levelAddRequest);
        return Result.success(levelId);
    }

    /**
     * 更新关卡
     */
    @Operation(summary = "更新关卡")
    @PutMapping("/levels")
    public Result<Boolean> updateLevel(@RequestBody LevelUpdateRequest levelUpdateRequest) {
        boolean success = levelService.updateLevel(levelUpdateRequest);
        return Result.success(success);
    }

    /**
     * 删除关卡
     */
    @Operation(summary = "删除关卡")
    @DeleteMapping("/levels/{levelId}")
    public Result<Boolean> deleteLevel(@PathVariable Long levelId) {
        boolean success = levelService.deleteLevel(levelId);
        return Result.success(success);
    }

    /**
     * 设置精选关卡（优先级自动设为99）
     */
    @Operation(summary = "设置精选关卡")
    @PostMapping("/levels/{levelId}/featured")
    public Result<Boolean> setFeaturedLevel(@PathVariable Long levelId) {
        boolean success = levelService.setFeaturedLevel(levelId, 99);
        return Result.success(success);
    }

    /**
     * 取消精选关卡（设置优先级为0）
     */
    @Operation(summary = "取消精选关卡")
    @PostMapping("/levels/{levelId}/cancel-featured")
    public Result<Boolean> cancelFeaturedLevel(@PathVariable Long levelId) {
        boolean success = levelService.setFeaturedLevel(levelId, 0);
        return Result.success(success);
    }

    /**
     * 设置用户角色
     */
    @Operation(summary = "设置用户角色")
    @PostMapping("/users/{userId}/role")
    public Result<Boolean> setUserRole(@PathVariable Long userId, @RequestParam String userRole) {
        if (!"user".equals(userRole) && !"admin".equals(userRole)) {
            return Result.error(40000, "用户角色只能是user或admin");
        }

        User user = userService.getById(userId);
        if (user == null) {
            return Result.error(40400, "用户不存在");
        }

        user.setUserRole(userRole);
        boolean success = userService.updateById(user);
        return Result.success(success);
    }
}