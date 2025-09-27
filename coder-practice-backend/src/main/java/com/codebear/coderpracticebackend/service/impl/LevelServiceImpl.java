package com.codebear.coderpracticebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codebear.coderpracticebackend.mapper.LevelMapper;
import com.codebear.coderpracticebackend.common.PageResult;
import com.codebear.coderpracticebackend.model.entity.Level;
import com.codebear.coderpracticebackend.service.LevelService;
import com.codebear.coderpracticebackend.service.ai.LevelGenerationService;
import com.codebear.coderpracticebackend.service.ai.dto.LevelGenerationResponse;
import com.codebear.coderpracticebackend.model.dto.level.LevelAddRequest;
import com.codebear.coderpracticebackend.model.dto.level.LevelUpdateRequest;
import com.codebear.coderpracticebackend.model.dto.level.LevelQueryRequest;
import com.codebear.coderpracticebackend.utils.JsonUtils;
import com.codebear.coderpracticebackend.exception.BusinessException;
import com.codebear.coderpracticebackend.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class LevelServiceImpl extends ServiceImpl<LevelMapper, Level> implements LevelService {

    @Autowired
    private LevelGenerationService levelGenerationService;

    @Override
    public PageResult<Level> getLevelList(LevelQueryRequest levelQueryRequest) {
        Page<Level> page = new Page<>(levelQueryRequest.getCurrent(), levelQueryRequest.getPageSize());

        QueryWrapper<Level> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDelete", 0);

        // 关卡名称模糊查询
        if (levelQueryRequest.getLevelName() != null && !levelQueryRequest.getLevelName().isEmpty()) {
            queryWrapper.like("levelName", levelQueryRequest.getLevelName());
        }

        // 难度查询
        if (levelQueryRequest.getDifficulty() != null && !levelQueryRequest.getDifficulty().isEmpty()) {
            queryWrapper.eq("difficulty", levelQueryRequest.getDifficulty());
        }

        // 精选状态查询 - 这是新增的功能
        if (levelQueryRequest.getIsFeatured() != null) {
            if (levelQueryRequest.getIsFeatured()) {
                // 查询精选关卡：priority > 0
                queryWrapper.gt("priority", 0);
                // 按优先级降序排序，优先级高的排在前面
                queryWrapper.orderByDesc("priority");
            } else {
                // 查询非精选关卡：priority = 0 或 priority is null
                queryWrapper.and(wrapper -> wrapper
                    .eq("priority", 0)
                    .or()
                    .isNull("priority")
                );
            }
        }

        // 按创建时间降序排序（精选关卡已按优先级排序）
        queryWrapper.orderByDesc("createTime");

        Page<Level> result = this.page(page, queryWrapper);

        return new PageResult<>(result.getRecords(), result.getTotal(),
            (int) result.getCurrent(), (int) result.getSize());
    }

    @Override
    public Level getLevelById(Long levelId) {
        Level level = this.getById(levelId);
        if (level == null || level.getIsDelete() == 1) {
            throw new RuntimeException("关卡不存在");
        }
        return level;
    }

    @Override
    public Level generateLevel(Integer salary) {
        return generateLevel(salary, null);
    }

    @Override
    public Level generateLevel(Integer salary, String direction) {
        log.info("开始生成关卡，用户薪资: {}, 岗位方向: {}", salary, direction);

        try {
            // 调用AI服务生成关卡
            LevelGenerationResponse response = levelGenerationService.generateLevel(salary, direction);

            // 转换为数据库实体
            Level level = new Level();
            level.setLevelName(response.getLevelName());
            level.setLevelDesc(response.getLevelDesc());
            level.setOptions(JsonUtils.toJson(response.getOptions()));
            level.setTargetSalary(salary);
            level.setDirection(direction);

            // 根据薪资确定难度等级
            level.setDifficulty(determineDifficulty(salary));
            level.setIsDelete(0);

            // 保存到数据库
            this.save(level);

            log.info("关卡生成成功，关卡ID: {}", level.getId());
            return level;

        } catch (Exception e) {
            log.error("AI生成关卡失败", e);
            // 返回一个默认关卡作为降级方案
            return generateFallbackLevel(salary, direction);
        }
    }

    /**
     * 根据薪资确定难度等级
     */
    private String determineDifficulty(Integer salary) {
        if (salary == null || salary < 5000) {
            return "初级";
        } else if (salary < 10000) {
            return "初级+";
        } else if (salary < 20000) {
            return "中级";
        } else if (salary < 35000) {
            return "高级";
        } else {
            return "专家级";
        }
    }

    /**
     * 生成降级方案（当AI生成失败时使用）
     */
    private Level generateFallbackLevel(Integer salary, String direction) {
        Level level = new Level();
        level.setLevelName("技术基础知识测试");
        level.setLevelDesc("这是一个基础的编程技术测试，包含常用开发知识。");
        level.setOptions("[{\"optionName\":\"Java\",\"trueAnswer\":true},{\"optionName\":\"Spring Boot\",\"trueAnswer\":true},{\"optionName\":\"MySQL\",\"trueAnswer\":true},{\"optionName\":\"HTML/CSS\",\"trueAnswer\":true},{\"optionName\":\"JavaScript\",\"trueAnswer\":true},{\"optionName\":\"Assembly\",\"trueAnswer\":false},{\"optionName\":\"COBOL\",\"trueAnswer\":false}]");
        level.setDifficulty(determineDifficulty(salary));
        level.setTargetSalary(salary);
        level.setDirection(direction);
        level.setIsDelete(0);

        this.save(level);
        return level;
    }

    @Override
    public Long addLevel(LevelAddRequest levelAddRequest) {
        // 参数校验
        if (!StringUtils.hasText(levelAddRequest.getLevelName())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "关卡名称不能为空");
        }
        if (!StringUtils.hasText(levelAddRequest.getLevelDesc())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "关卡描述不能为空");
        }
        if (!StringUtils.hasText(levelAddRequest.getOptions())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "关卡选项不能为空");
        }
        if (!StringUtils.hasText(levelAddRequest.getDifficulty())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "难度等级不能为空");
        }

        // 创建关卡实体
        Level level = new Level();
        BeanUtils.copyProperties(levelAddRequest, level);
        level.setIsDelete(0);

        // 保存关卡
        this.save(level);

        log.info("管理员添加关卡成功，关卡ID: {}", level.getId());
        return level.getId();
    }

    @Override
    public boolean updateLevel(LevelUpdateRequest levelUpdateRequest) {
        // 参数校验
        if (levelUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "关卡ID不能为空");
        }

        // 查询关卡是否存在
        Level existingLevel = this.getById(levelUpdateRequest.getId());
        if (existingLevel == null || existingLevel.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "关卡不存在");
        }

        // 更新关卡信息
        Level level = new Level();
        BeanUtils.copyProperties(levelUpdateRequest, level);

        // 更新操作
        boolean success = this.updateById(level);

        if (success) {
            log.info("管理员更新关卡成功，关卡ID: {}", levelUpdateRequest.getId());
        }
        return success;
    }

    @Override
    public boolean deleteLevel(Long levelId) {
        // 参数校验
        if (levelId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "关卡ID不能为空");
        }

        // 查询关卡是否存在
        Level level = this.getById(levelId);
        if (level == null || level.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "关卡不存在");
        }

        // 逻辑删除
        level.setIsDelete(1);
        boolean success = this.updateById(level);

        if (success) {
            log.info("管理员删除关卡成功，关卡ID: {}", levelId);
        }
        return success;
    }

    @Override
    public boolean setFeaturedLevel(Long levelId, Integer priority) {
        // 参数校验
        if (levelId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "关卡ID不能为空");
        }
        if (priority == null || priority < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "优先级必须大于等于0");
        }

        // 查询关卡是否存在
        Level level = this.getById(levelId);
        if (level == null || level.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "关卡不存在");
        }

        // 更新优先级
        level.setPriority(priority);
        boolean success = this.updateById(level);

        if (success) {
            log.info("管理员设置精选关卡成功，关卡ID: {}, 优先级: {}", levelId, priority);
        }
        return success;
    }

    @Override
    public java.util.List<Level> getAllLevelsOrderByPriority() {
        QueryWrapper<Level> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDelete", 0);
        queryWrapper.orderByDesc("priority");
        queryWrapper.orderByDesc("createTime");

        return this.list(queryWrapper);
    }

    @Override
    public Page<Level> getLevelsByPage(LevelQueryRequest levelQueryRequest) {
        Page<Level> page = new Page<>(levelQueryRequest.getCurrent(), levelQueryRequest.getPageSize());

        QueryWrapper<Level> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDelete", 0);

        // 关卡名称模糊查询
        if (levelQueryRequest.getLevelName() != null && !levelQueryRequest.getLevelName().isEmpty()) {
            queryWrapper.like("levelName", levelQueryRequest.getLevelName());
        }

        // 难度查询
        if (levelQueryRequest.getDifficulty() != null && !levelQueryRequest.getDifficulty().isEmpty()) {
            queryWrapper.eq("difficulty", levelQueryRequest.getDifficulty());
        }

        // 精选状态查询
        if (levelQueryRequest.getIsFeatured() != null) {
            if (levelQueryRequest.getIsFeatured()) {
                queryWrapper.gt("priority", 0);
            } else {
                queryWrapper.eq("priority", 0);
            }
        }

        // 按创建时间降序排序
        queryWrapper.orderByDesc("createTime");

        return this.page(page, queryWrapper);
    }
}