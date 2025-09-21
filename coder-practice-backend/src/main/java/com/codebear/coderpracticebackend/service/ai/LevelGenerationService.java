package com.codebear.coderpracticebackend.service.ai;

import com.codebear.coderpracticebackend.service.ai.dto.LevelGenerationRequest;
import com.codebear.coderpracticebackend.service.ai.dto.LevelGenerationResponse;

/**
 * AI关卡生成服务接口
 */
public interface LevelGenerationService {

    /**
     * 根据用户薪资生成关卡
     * @param request 包含用户薪资的请求
     * @return 生成的关卡信息
     */
    LevelGenerationResponse generateLevel(LevelGenerationRequest request);
}