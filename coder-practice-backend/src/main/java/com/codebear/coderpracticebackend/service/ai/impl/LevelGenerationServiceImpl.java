package com.codebear.coderpracticebackend.service.ai.impl;

import com.codebear.coderpracticebackend.service.ai.LevelGenerationService;
import com.codebear.coderpracticebackend.service.ai.dto.LevelGenerationRequest;
import com.codebear.coderpracticebackend.service.ai.dto.LevelGenerationResponse;
import com.codebear.coderpracticebackend.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;

/**
 * AI关卡生成服务实现
 */
@Slf4j
@Service
public class LevelGenerationServiceImpl implements LevelGenerationService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${langchain4j.community.dashscope.chat-model.api-key:}")
    private String apiKey;

    @Value("${langchain4j.community.dashscope.chat-model.model-name:qwen-max}")
    private String modelName;

    @Override
    public LevelGenerationResponse generateLevel(LevelGenerationRequest request) {
        log.info("AI生成关卡开始，薪资: {}", request.getSalary());

        try {
            // 读取提示词文件
            String systemPrompt = readPromptFile("prompts/level-generation-prompt.txt");

            // 构建用户消息
            String userMessage = String.format("当前薪资：%d", request.getSalary());

            // 调用AI服务 (这里暂时返回模拟数据)
            return generateMockResponse(request.getSalary());

        } catch (Exception e) {
            log.error("AI生成关卡失败", e);
            // 返回默认关卡
            return generateDefaultResponse(request.getSalary());
        }
    }

    /**
     * 生成模拟响应 (暂时替代真实AI调用)
     */
    private LevelGenerationResponse generateMockResponse(Integer salary) {
        LevelGenerationResponse response = new LevelGenerationResponse();

        // 初始化选项列表
        response.setOptions(new ArrayList<>());

        // 根据薪资调整难度
        String difficulty = determineDifficulty(salary);

        response.setLevelName(difficulty + "开发挑战");
        response.setLevelDesc("这是一个基于薪资" + salary + "元生成的开发挑战关卡。请根据你的技术知识选择合适的实现方案。");

        // 生成选项
        response.getOptions().add(createOption("Spring Boot", true));
        response.getOptions().add(createOption("MySQL", true));
        response.getOptions().add(createOption("Redis", true));
        response.getOptions().add(createOption("Docker", true));
        response.getOptions().add(createOption("Git", true));
        response.getOptions().add(createOption("COBOL", false));
        response.getOptions().add(createOption("FORTRAN", false));
        response.getOptions().add(createOption("手动管理内存", false));
        response.getOptions().add(createOption("不用版本控制", false));
        response.getOptions().add(createOption("硬编码配置", false));

        return response;
    }

    /**
     * 生成默认响应
     */
    private LevelGenerationResponse generateDefaultResponse(Integer salary) {
        LevelGenerationResponse response = new LevelGenerationResponse();
        response.setOptions(new ArrayList<>());
        response.setLevelName("技术基础测试");
        response.setLevelDesc("这是一个基础的技术知识测试关卡。");

        response.getOptions().add(createOption("Java", true));
        response.getOptions().add(createOption("Spring Boot", true));
        response.getOptions().add(createOption("MySQL", true));
        response.getOptions().add(createOption("Assembly", false));
        response.getOptions().add(createOption("COBOL", false));

        return response;
    }

    /**
     * 根据薪资确定难度
     */
    private String determineDifficulty(Integer salary) {
        if (salary == null || salary < 5000) {
            return "初级";
        } else if (salary < 10000) {
            return "初级+";
        } else if (salary < 20000) {
            return "中级";
        } else if (salary < 35000) {
            return "高级";
        } else {
            return "专家级";
        }
    }

    /**
     * 创建选项
     */
    private LevelGenerationResponse.Option createOption(String name, boolean correct) {
        LevelGenerationResponse.Option option = new LevelGenerationResponse.Option();
        option.setOptionName(name);
        option.setTrueAnswer(correct);
        return option;
    }

    /**
     * 读取提示词文件
     */
    private String readPromptFile(String filePath) {
        try {
            // 这里简化处理，实际应该读取resources目录下的文件
            return "你是一位程序员专家，需要根据薪资生成关卡";
        } catch (Exception e) {
            log.error("读取提示词文件失败", e);
            return "根据薪资生成关卡";
        }
    }
}