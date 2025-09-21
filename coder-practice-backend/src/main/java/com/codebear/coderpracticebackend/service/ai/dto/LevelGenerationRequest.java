package com.codebear.coderpracticebackend.service.ai.dto;

import lombok.Data;

@Data
public class LevelGenerationRequest {
    /**
     * 用户当前薪资
     */
    private Integer salary;
}