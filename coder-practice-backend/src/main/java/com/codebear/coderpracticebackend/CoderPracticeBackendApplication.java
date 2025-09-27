package com.codebear.coderpracticebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * 编程练习系统后端应用
 *
 * 集成LangChain4j AI Services，提供智能关卡生成和结果报告功能
 * 启用重试机制确保服务的高可用性
 */
@SpringBootApplication
@EnableRetry
public class CoderPracticeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoderPracticeBackendApplication.class, args);
    }

}
