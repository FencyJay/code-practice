package com.codebear.coderpracticebackend.service.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResultReportRequest {
    /**
     * 关卡名称
     */
    private String levelName;

    /**
     * 关卡需求描述
     */
    private String levelDesc;

    /**
     * 用户选择的选项
     */
    private List<ReportOption> userOptions;

    /**
     * 正确选项
     */
    private List<String> trueOptions;

    /**
     * 用户当前薪资
     */
    private Integer salary;
}