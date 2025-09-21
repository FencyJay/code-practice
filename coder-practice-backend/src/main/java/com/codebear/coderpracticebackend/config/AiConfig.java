package com.codebear.coderpracticebackend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * AI功能配置类
 * LangChain4j会自动配置AI服务，无需手动配置Bean
 */
@Slf4j
@Configuration
public class AiConfig {

    public AiConfig() {
        log.info("AI配置类已初始化，LangChain4j服务将自动配置");
    }
}