package com.codebear.coderpracticebackend.service.ai.dto;

import lombok.Data;

/**
 * 报告选项
 */
@Data
public class ReportOption {
    /**
     * 选项名称
     */
    private String optionName;

    /**
     * 是否为正确答案
     */
    private Boolean trueAnswer;
}