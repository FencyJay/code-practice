package com.codebear.coderpracticebackend.service.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class LevelGenerationResponse {
    /**
     * 关卡名称
     */
    private String levelName;

    /**
     * 关卡需求描述
     */
    private String levelDesc;

    /**
     * 关卡选项
     */
    private List<LevelOption> options;
}