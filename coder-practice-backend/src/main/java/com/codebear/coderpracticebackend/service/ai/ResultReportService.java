package com.codebear.coderpracticebackend.service.ai;

import com.codebear.coderpracticebackend.service.ai.dto.ReportOption;
import com.codebear.coderpracticebackend.service.ai.dto.ResultReportResponse;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

import java.util.List;

/**
 * AI结果报告生成服务
 */
@AiService
public interface ResultReportService {

    /**
     * 根据用户答题情况生成结果报告
     * @param levelName 关卡名称
     * @param levelDesc 关卡需求描述
     * @param userOptions 用户选择的选项
     * @param trueOptions 正确选项
     * @param salary 用户当前薪资
     * @return 生成的结果报告
     */
    @SystemMessage("你是一位资深的技术专家和职业发展顾问，需要为程序员提供专业的技术评估报告。")
    @UserMessage("请基于以下信息生成技术评估报告：\n" +
            "关卡名称：{{levelName}}\n" +
            "需求描述：{{levelDesc}}\n" +
            "用户选择：{{userOptions}}\n" +
            "正确答案：{{trueOptions}}\n" +
            "当前薪资：{{salary}}元\n\n" +
            "请严格按照JSON格式输出评估结果，包含评分、评价、薪资变化、建议、原因、正确选项和标准答案解析。")
    ResultReportResponse generateReport(
            @V("levelName") String levelName,
            @V("levelDesc") String levelDesc,
            @V("userOptions") List<ReportOption> userOptions,
            @V("trueOptions") List<String> trueOptions,
            @V("salary") Integer salary
    );
}