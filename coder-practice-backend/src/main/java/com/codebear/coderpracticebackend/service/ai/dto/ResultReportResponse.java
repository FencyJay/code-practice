package com.codebear.coderpracticebackend.service.ai.dto;

import com.codebear.coderpracticebackend.service.ai.tool.InterviewQuestionTool;
import lombok.Data;

import java.util.List;

@Data
public class ResultReportResponse {
    /**
     * 得分（0-100）
     */
    private Integer score;

    /**
     * 评价
     */
    private String comment;

    /**
     * 薪资变化（正数为加薪，负数为减薪）
     */
    private Integer salaryChange;

    /**
     * 投递建议
     */
    private String suggest;

    /**
     * 评分原因
     */
    private String reason;

    /**
     * 正确选项列表
     */
    private List<String> trueOptions;

    /**
     * 标准答案解析
     */
    private String standardAnswer;

    /**
     * 推荐面试题列表
     */
    private List<InterviewQuestionTool.InterviewQuestion> recommendedQuestions;
}