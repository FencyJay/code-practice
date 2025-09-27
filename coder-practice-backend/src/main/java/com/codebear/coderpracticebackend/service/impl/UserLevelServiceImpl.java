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
import com.codebear.coderpracticebackend.service.ai.ResultReportService;
import com.codebear.coderpracticebackend.service.ai.dto.LevelOption;
import com.codebear.coderpracticebackend.service.UserLevelService;
import com.codebear.coderpracticebackend.service.LevelService;
import com.codebear.coderpracticebackend.service.UserService;
import com.codebear.coderpracticebackend.service.ai.dto.ResultReportResponse;
import com.codebear.coderpracticebackend.service.ai.dto.ReportOption;
import com.codebear.coderpracticebackend.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.service.spring.AiService;
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

            // 准备AI服务调用参数
            List<ReportOption> userOptions = submitRequest.getSelectedOptions().stream()
                    .map(optionName -> {
                        ReportOption optionDto = new ReportOption();
                        optionDto.setOptionName(optionName);
                        optionDto.setTrueAnswer(true); // 默认设置为true，实际应该根据正确答案判断
                        return optionDto;
                    })
                    .toList();

            // 获取正确选项列表
            List<String> trueOptions = JsonUtils.fromJson(level.getOptions(), new TypeReference<List<LevelOption>>() {})
                    .stream()
                    .filter(LevelOption::getTrueAnswer)
                    .map(LevelOption::getOptionName)
                    .toList();

            // 调用AI服务生成结果报告，将复杂对象转换为JSON字符串
            String userOptionsJson = JsonUtils.toJson(userOptions);
            ResultReportResponse response = resultReportService.generateReport(
                    level.getLevelName(),
                    level.getLevelDesc(),
                    userOptionsJson,
                    trueOptions,
                    user.getSalary()
            );

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

        // 保存推荐面试题
        if (response.getRecommendedQuestions() != null && !response.getRecommendedQuestions().isEmpty()) {
            try {
                userLevel.setRecommendedQuestions(objectMapper.writeValueAsString(response.getRecommendedQuestions()));
            } catch (JsonProcessingException e) {
                log.warn("保存推荐面试题失败：{}", e.getMessage());
                userLevel.setRecommendedQuestions("[]");
            }
        } else {
            userLevel.setRecommendedQuestions("[]");
        }

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

    @Override
    public UserLevel getUserLevelResult(Long id, Long currentUserId) {
        log.info("获取用户闯关结果详情，记录ID: {}, 当前用户ID: {}", id, currentUserId);

        // 查询闯关记录
        UserLevel userLevel = this.getById(id);
        if (userLevel == null) {
            log.warn("闯关记录不存在，ID: {}", id);
            throw new RuntimeException("闯关记录不存在");
        }

        // 检查是否已删除
        if (userLevel.getIsDelete() == 1) {
            log.warn("闯关记录已删除，ID: {}", id);
            throw new RuntimeException("闯关记录已删除");
        }

        // 权限验证：只能查看自己的闯关记录
        if (!userLevel.getUserId().equals(currentUserId)) {
            log.warn("用户无权访问此闯关记录，记录ID: {}, 当前用户ID: {}, 记录所属用户ID: {}",
                     id, currentUserId, userLevel.getUserId());
            throw new RuntimeException("无权访问此闯关记录");
        }

        log.info("成功获取闯关记录详情，ID: {}, 用户ID: {}, 得分: {}",
                 id, userLevel.getUserId(), userLevel.getScore());
        return userLevel;
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
        userLevel.setRecommendedQuestions("[]");
        userLevel.setIsDelete(0);

        this.save(userLevel);
        return userLevel;
    }
}