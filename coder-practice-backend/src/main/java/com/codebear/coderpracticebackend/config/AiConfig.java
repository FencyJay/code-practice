package com.codebear.coderpracticebackend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * AI功能配置类
 * 现在使用混合配置模式，手动创建AI服务
 */
@Slf4j
@Configuration
public class AiConfig {

    public AiConfig() {
        log.info("AI配置类已初始化，使用混合配置模式");
    }
}