package com.codebear.coderpracticebackend.config;

import com.codebear.coderpracticebackend.interceptor.AdminInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 *
 * @author fency
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 管理员权限拦截器，拦截所有/admin开头的请求
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");
    }
}