package com.codebear.coderpracticebackend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * AI功能配置类
 */
@Slf4j
@Configuration
public class AiConfig {

    /**
     * 配置RestTemplate用于HTTP请求
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * AI服务配置说明
     * 目前使用手动实现AI服务，后续可以集成LangChain4j
     */

    public AiConfig() {
        log.info("AI配置类已初始化");
    }
}