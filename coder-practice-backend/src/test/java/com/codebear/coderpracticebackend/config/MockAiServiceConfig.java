package com.codebear.coderpracticebackend.config;

import com.codebear.coderpracticebackend.service.ai.LevelGenerationService;
import com.codebear.coderpracticebackend.service.ai.ResultReportService;
import com.codebear.coderpracticebackend.service.ai.dto.LevelGenerationResponse;
import com.codebear.coderpracticebackend.service.ai.dto.LevelOption;
import com.codebear.coderpracticebackend.service.ai.dto.ResultReportResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试环境 AI 服务 Mock 配置。
 */
@Configuration
@Profile("test")
public class MockAiServiceConfig {

    @Bean
    @Primary
    public LevelGenerationService mockLevelGenerationService() {
        return new LevelGenerationService() {
            @Override
            public LevelGenerationResponse generateLevel(Integer salary) {
                return buildLevel("通用");
            }

            @Override
            public LevelGenerationResponse generateLevel(Integer salary, String direction) {
                return buildLevel(direction);
            }

            private LevelGenerationResponse buildLevel(String direction) {
                LevelGenerationResponse response = new LevelGenerationResponse();
                response.setLevelName("Mock关卡-" + direction);
                response.setLevelDesc("这是测试环境生成的模拟关卡");

                LevelOption optionA = new LevelOption();
                optionA.setKey("A");
                optionA.setValue("模拟选项A");

                LevelOption optionB = new LevelOption();
                optionB.setKey("B");
                optionB.setValue("模拟选项B");

                response.setOptions(List.of(optionA, optionB));
                return response;
            }
        };
    }

    @Bean
    @Primary
    public ResultReportService mockResultReportService() {
        return (levelName, levelDesc, userOptionsJson, trueOptions, salary) -> {
            ResultReportResponse response = new ResultReportResponse();
            response.setScore(80);
            response.setComment("测试环境模拟报告");
            response.setSalaryChange(500);
            response.setSuggest("继续强化基础与项目实践");
            response.setReason("Mock 服务返回固定分析结果");
            response.setTrueOptions(trueOptions);
            response.setStandardAnswer("这是用于测试的标准答案解析");
            response.setRecommendedQuestions(new ArrayList<>());
            return response;
        };
    }
}
