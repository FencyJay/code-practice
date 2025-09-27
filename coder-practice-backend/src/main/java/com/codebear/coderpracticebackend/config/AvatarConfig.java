package com.codebear.coderpracticebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 头像资源配置类
 */
@Configuration
public class AvatarConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置默认头像资源的访问路径
        registry.addResourceHandler("/api/default-avatars/**")
                .addResourceLocations("classpath:/static/avatars/");
    }
}