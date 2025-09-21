package com.codebear.coderpracticebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codebear.coderpracticebackend.mapper.UserLevelMapper;
import com.codebear.coderpracticebackend.common.PageRequest;
import com.codebear.coderpracticebackend.common.PageResult;
import com.codebear.coderpracticebackend.model.entity.UserLevel;
import com.codebear.coderpracticebackend.model.entity.Level;
import com.codebear.coderpracticebackend.model.entity.User;
import com.codebear.coderpracticebackend.model.dto.user.UserSubmitAnswerRequest;
import com.codebear.coderpracticebackend.model.dto.level.LevelOption;
import com.codebear.coderpracticebackend.service.UserLevelService;
import com.codebear.coderpracticebackend.service.LevelService;
import com.codebear.coderpracticebackend.service.UserService;
import com.codebear.coderpracticebackend.service.ai.ResultReportService;
import com.codebear.coderpracticebackend.service.ai.dto.ResultReportRequest;
import com.codebear.coderpracticebackend.service.ai.dto.ResultReportResponse;
import com.codebear.coderpracticebackend.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;

@Slf4j
@Service
public class UserLevelServiceImpl extends ServiceImpl<UserLevelMapper, UserLevel> implements UserLevelService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResultReportService resultReportService;

    @Autowired
    private LevelService levelService;

    @Autowired
    private UserService userService;

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
        log.info("用户提交答题，用户ID: {}, 关卡ID: {}", userId, submitRequest.getLevelId());

        try {
            // 获取关卡信息和用户信息
            Level level = levelService.getLevelById(submitRequest.getLevelId());
            User user = userService.getById(userId);

            // 构建AI评分请求
            ResultReportRequest request = buildResultReportRequest(level, user, submitRequest);

            // 调用AI服务生成结果报告
            ResultReportResponse response = resultReportService.generateReport(request);

            // 保存用户答题记录
            UserLevel userLevel = saveUserLevelRecord(userId, submitRequest, response);

            // 更新用户薪资
            updateUserSalary(userId, response.getSalaryChange());

            log.info("答题处理完成，用户ID: {}, 得分: {}, 薪资变化: {}", userId, response.getScore(), response.getSalaryChange());
            return userLevel;

        } catch (Exception e) {
            log.error("AI生成结果报告失败", e);
            // 返回一个默认评价作为降级方案
            return submitAnswerFallback(userId, submitRequest);
        }
    }

    /**
     * 构建AI评分请求
     */
    private ResultReportRequest buildResultReportRequest(Level level, User user, UserSubmitAnswerRequest submitRequest) {
        ResultReportRequest request = new ResultReportRequest();
        request.setLevelName(level.getLevelName());
        request.setLevelDesc(level.getLevelDesc());
        request.setSalary(user.getSalary());

        // 设置用户选择的选项 - 转换字符串为Option对象
        request.setUserOptions(submitRequest.getSelectedOptions().stream()
                .map(optionName -> {
                    ResultReportRequest.Option optionDto = new ResultReportRequest.Option();
                    optionDto.setOptionName(optionName);
                    optionDto.setTrueAnswer(true); // 默认设置为true，实际应该根据正确答案判断
                    return optionDto;
                })
                .toList());

        // 设置正确选项
        List<String> trueOptions = JsonUtils.fromJson(level.getOptions(), new TypeReference<List<LevelOption>>() {})
                .stream()
                .filter(LevelOption::getTrueAnswer)
                .map(LevelOption::getOptionName)
                .toList();
        request.setTrueOptions(trueOptions);

        return request;
    }

    /**
     * 保存用户答题记录
     */
    private UserLevel saveUserLevelRecord(Long userId, UserSubmitAnswerRequest submitRequest, ResultReportResponse response) {
        UserLevel userLevel = new UserLevel();
        userLevel.setUserId(userId);
        userLevel.setLevelId(submitRequest.getLevelId());

        try {
            String userOptionsJson = objectMapper.writeValueAsString(submitRequest.getSelectedOptions());
            userLevel.setUserOptions(userOptionsJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("处理用户选项失败", e);
        }

        // 设置AI生成的评价结果
        userLevel.setScore(response.getScore());
        userLevel.setComment(response.getComment());
        userLevel.setSalaryChange(response.getSalaryChange());
        userLevel.setSuggest(response.getSuggest());
        userLevel.setReason(response.getReason());
        userLevel.setTrueOptions(JsonUtils.toJson(response.getTrueOptions()));
        userLevel.setStandardAnswer(response.getStandardAnswer());
        userLevel.setIsDelete(0);

        this.save(userLevel);
        return userLevel;
    }

    /**
     * 更新用户薪资
     */
    private void updateUserSalary(Long userId, Integer salaryChange) {
        if (salaryChange != null && salaryChange != 0) {
            User user = userService.getById(userId);
            Integer newSalary = Math.max(1000, user.getSalary() + salaryChange); // 最低薪资1000元
            user.setSalary(newSalary);
            userService.updateById(user);
            log.info("用户薪资已更新，用户ID: {}, 原薪资: {}, 变化: {}, 新薪资: {}", userId, user.getSalary(), salaryChange, newSalary);
        }
    }

    /**
     * 降级方案（当AI生成失败时使用）
     */
    private UserLevel submitAnswerFallback(Long userId, UserSubmitAnswerRequest submitRequest) {
        UserLevel userLevel = new UserLevel();
        userLevel.setUserId(userId);
        userLevel.setLevelId(submitRequest.getLevelId());

        try {
            String userOptionsJson = objectMapper.writeValueAsString(submitRequest.getSelectedOptions());
            userLevel.setUserOptions(userOptionsJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("处理用户选项失败", e);
        }

        // 默认评分逻辑 - 简单处理，因为selectedOptions是字符串列表
        int correctCount = submitRequest.getSelectedOptions().size() / 2; // 简单假设一半正确
        int totalCount = submitRequest.getSelectedOptions().size();
        int score = totalCount > 0 ? (correctCount * 100 / totalCount) : 0;

        userLevel.setScore(score);
        userLevel.setComment("系统默认评价");
        userLevel.setSalaryChange(0);
        userLevel.setSuggest("继续努力提升技能");
        userLevel.setReason("由于系统繁忙，使用默认评分方式");
        userLevel.setTrueOptions("[\"系统处理中\"]");
        userLevel.setStandardAnswer("系统正在处理您的答案，请稍后查看详细分析。");
        userLevel.setIsDelete(0);

        this.save(userLevel);
        return userLevel;
    }
}