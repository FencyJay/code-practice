package com.codebear.coderpracticebackend.service.ai;

import com.codebear.coderpracticebackend.service.ai.dto.LevelGenerationRequest;
import com.codebear.coderpracticebackend.service.ai.dto.LevelGenerationResponse;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * AI关卡生成服务
 */
@AiService
public interface LevelGenerationService {

    /**
     * 根据用户薪资生成关卡
     *
     * @return 生成的关卡信息
     */
    @SystemMessage(fromResource = "prompts/level-generation-prompt.txt")
    LevelGenerationResponse generateLevel(Integer salary);
}