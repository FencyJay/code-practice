package com.codebear.coderpracticebackend.config;

import com.codebear.coderpracticebackend.service.ai.LevelGenerationService;
import com.codebear.coderpracticebackend.service.ai.ResultReportService;
import com.codebear.coderpracticebackend.service.ai.tool.InterviewQuestionTool;


import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI Services配置类
 *
 * 基于LangChain4j标准模式创建AI服务代理
 * 使用Spring Boot自动配置的ChatModel，遵循官方最佳实践
 */
@Slf4j
@Configuration
public class AiServiceConfig {


    /**
     * 创建关卡生成AI服务
     * 使用简单的AI Services模式，无需工具和记忆
     *
     * @param openAiChatModel 由Spring Boot自动配置的ChatModel
     * @return 关卡生成服务代理
     */
    @Bean
    public LevelGenerationService levelGenerationService(OpenAiChatModel openAiChatModel) {
        log.info("创建关卡生成AI服务代理");
        return AiServices.create(LevelGenerationService.class, openAiChatModel);
    }

    /**
     * 创建结果报告AI服务
     * 使用工具模式，但不使用聊天记忆
     *
     * @param openAiChatModel 由Spring Boot自动配置的ChatModel
     * @param interviewQuestionTool 面试题搜索工具
     * @return 结果报告服务代理
     */
    @Bean
    public ResultReportService resultReportService(
            OpenAiChatModel openAiChatModel,
            InterviewQuestionTool interviewQuestionTool) {
        log.info("创建结果报告AI服务代理，添加工具功能");

        // 使用工具模式，但不使用聊天记忆
        return AiServices.builder(ResultReportService.class)
                .chatModel(openAiChatModel)
               // .tools(interviewQuestionTool)
                .build();
    }
}