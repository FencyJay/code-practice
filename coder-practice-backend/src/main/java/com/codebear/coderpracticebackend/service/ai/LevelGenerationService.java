package com.codebear.coderpracticebackend.service.ai;

import com.codebear.coderpracticebackend.service.ai.dto.LevelGenerationResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * AI关卡生成服务接口
 *
 * 基于LangChain4j AI Services标准模式，提供智能关卡生成功能
 * 遵循官方最佳实践，使用注解方式定义AI服务接口
 */
public interface LevelGenerationService {

    /**
     * 根据用户薪资生成关卡
     *
     * @param salary 用户薪资
     * @return 生成的关卡信息
     */
    @SystemMessage(fromResource = "prompts/level-generation-prompt.txt")
    LevelGenerationResponse generateLevel(@V("salary") Integer salary);

    /**
     * 根据用户薪资和岗位方向生成关卡
     *
     * @param salary 用户薪资
     * @param direction 岗位方向
     * @return 生成的关卡信息
     */
    @SystemMessage(fromResource = "prompts/level-generation-with-direction-prompt.txt")
    @UserMessage("请基于以下信息生成关卡：\n" +
            "当前薪资：{{salary}}元\n\n" +
            "岗位方向：{{direction}}\n"
    )
    LevelGenerationResponse generateLevel(
            @V("salary") Integer salary,
            @V("direction") String direction
    );
}