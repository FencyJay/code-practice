package com.codebear.coderpracticebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codebear.coderpracticebackend.mapper.LevelMapper;
import com.codebear.coderpracticebackend.common.PageRequest;
import com.codebear.coderpracticebackend.common.PageResult;
import com.codebear.coderpracticebackend.model.entity.Level;
import com.codebear.coderpracticebackend.service.LevelService;
import com.codebear.coderpracticebackend.service.ai.LevelGenerationService;
import com.codebear.coderpracticebackend.service.ai.dto.LevelGenerationRequest;
import com.codebear.coderpracticebackend.service.ai.dto.LevelGenerationResponse;
import com.codebear.coderpracticebackend.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LevelServiceImpl extends ServiceImpl<LevelMapper, Level> implements LevelService {

    @Autowired
    private LevelGenerationService levelGenerationService;

    @Override
    public PageResult<Level> getLevelList(PageRequest pageRequest, String difficulty) {
        Page<Level> page = new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize());

        QueryWrapper<Level> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDelete", 0);

        if (difficulty != null && !difficulty.isEmpty()) {
            queryWrapper.eq("difficulty", difficulty);
        }

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
        log.info("开始生成关卡，用户薪资: {}", salary);

        try {
            // 构建AI生成请求
            LevelGenerationRequest request = new LevelGenerationRequest();
            request.setSalary(salary);

            // 调用AI服务生成关卡
            LevelGenerationResponse response = levelGenerationService.generateLevel(request);

            // 转换为数据库实体
            Level level = new Level();
            level.setLevelName(response.getLevelName());
            level.setLevelDesc(response.getLevelDesc());
            level.setOptions(JsonUtils.toJson(response.getOptions()));
            level.setTargetSalary(salary);

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
            return generateFallbackLevel(salary);
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
    private Level generateFallbackLevel(Integer salary) {
        Level level = new Level();
        level.setLevelName("技术基础知识测试");
        level.setLevelDesc("这是一个基础的编程技术测试，包含常用开发知识。");
        level.setOptions("[{\"optionName\":\"Java\",\"trueAnswer\":true},{\"optionName\":\"Spring Boot\",\"trueAnswer\":true},{\"optionName\":\"MySQL\",\"trueAnswer\":true},{\"optionName\":\"HTML/CSS\",\"trueAnswer\":true},{\"optionName\":\"JavaScript\",\"trueAnswer\":true},{\"optionName\":\"Assembly\",\"trueAnswer\":false},{\"optionName\":\"COBOL\",\"trueAnswer\":false}]");
        level.setDifficulty(determineDifficulty(salary));
        level.setTargetSalary(salary);
        level.setIsDelete(0);

        this.save(level);
        return level;
    }
}