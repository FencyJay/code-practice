package com.codebear.coderpracticebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codebear.coderpracticebackend.mapper.LevelMapper;
import com.codebear.coderpracticebackend.common.PageRequest;
import com.codebear.coderpracticebackend.common.PageResult;
import com.codebear.coderpracticebackend.model.entity.Level;
import com.codebear.coderpracticebackend.service.LevelService;
import org.springframework.stereotype.Service;

@Service
public class LevelServiceImpl extends ServiceImpl<LevelMapper, Level> implements LevelService {

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
        // TODO: AI生成关卡逻辑 - 根据薪资调用AI生成对应难度的关卡
        // 目前返回一个固定的测试关卡

        Level level = new Level();
        level.setLevelName("测试关卡");
        level.setLevelDesc("这是一个测试关卡的需求描述");
        level.setOptions("[{\"optionName\":\"Spring Boot\",\"trueAnswer\":true},{\"optionName\":\"MySQL\",\"trueAnswer\":true},{\"optionName\":\"C++\",\"trueAnswer\":false}]");
        level.setDifficulty("中等");
        level.setTargetSalary(salary);
        level.setIsDelete(0);

        this.save(level);

        return level;
    }
}