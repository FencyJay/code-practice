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
    private List<Option> options;

    @Data
    public static class Option {
        /**
         * 选项名称
         */
        private String optionName;

        /**
         * 是否为正确答案
         */
        private Boolean trueAnswer;
    }
}