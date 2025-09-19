package com.codebear.coderpracticebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codebear.coderpracticebackend.mapper.UserLevelMapper;
import com.codebear.coderpracticebackend.common.PageRequest;
import com.codebear.coderpracticebackend.common.PageResult;
import com.codebear.coderpracticebackend.model.entity.UserLevel;
import com.codebear.coderpracticebackend.model.dto.user.UserSubmitAnswerRequest;
import com.codebear.coderpracticebackend.service.UserLevelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLevelServiceImpl extends ServiceImpl<UserLevelMapper, UserLevel> implements UserLevelService {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PageResult<UserLevel> getUserLevelHistory(Long userId, PageRequest pageRequest) {
        Page<UserLevel> page = new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize());

        QueryWrapper<UserLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("isDelete", 0);
        queryWrapper.orderByDesc("createTime");

        Page<UserLevel> result = this.page(page, queryWrapper);

        return new PageResult<>(result.getRecords(), result.getTotal(),
            (int) result.getCurrent(), (int) result.getSize());
    }

    @Override
    public UserLevel submitAnswer(Long userId, UserSubmitAnswerRequest submitRequest) {
        UserLevel userLevel = new UserLevel();
        userLevel.setUserId(userId);
        userLevel.setLevelId(submitRequest.getLevelId());

        try {
            String userOptionsJson = objectMapper.writeValueAsString(submitRequest.getSelectedOptions());
            userLevel.setUserOptions(userOptionsJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("处理用户选项失败", e);
        }

        // TODO: AI评分逻辑 - 调用AI根据用户选择的选项生成评分结果
        // 目前返回固定的测试数据
        userLevel.setScore(85);
        userLevel.setComment("测试评价");
        userLevel.setSalaryChange(1000);
        userLevel.setSuggest("测试建议");
        userLevel.setReason("测试原因");
        userLevel.setTrueOptions("[\"Spring Boot\", \"MySQL\"]");
        userLevel.setStandardAnswer("测试标准答案");
        userLevel.setIsDelete(0);

        this.save(userLevel);

        return userLevel;
    }
}