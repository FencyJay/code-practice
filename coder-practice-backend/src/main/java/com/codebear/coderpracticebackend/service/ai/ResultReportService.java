package com.codebear.coderpracticebackend.service.ai;

import com.codebear.coderpracticebackend.service.ai.dto.ReportOption;
import com.codebear.coderpracticebackend.service.ai.dto.ResultReportResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

/**
 * AI结果报告生成服务接口
 *
 * 基于LangChain4j AI Services标准模式，提供智能结果报告生成功能
 * 集成面试题搜索工具，能够根据用户答题情况提供个性化建议
 * 遵循官方最佳实践，使用注解方式定义AI服务接口
 */
public interface ResultReportService {

    /**
     * 根据用户答题情况生成结果报告
     *
     * @param levelName 关卡名称
     * @param levelDesc 关卡需求描述
     * @param trueOptions 正确选项
     * @param salary 用户当前薪资
     * @return 生成的结果报告
     */
    @SystemMessage(fromResource = "prompts/result-report-prompt.txt")
    @UserMessage("请基于以下信息生成技术评估报告：\n" +
            "关卡名称：{{levelName}}\n" +
            "需求描述：{{levelDesc}}\n" +
            "用户选择：{{userOptions}}\n" +
            "正确答案：{{trueOptions}}\n" +
            "当前薪资：{{salary}}元\n\n" +
            "请按JSON格式输出，包含：score(评分0-100)、comment(评价)、salaryChange(薪资变化)、" +
            "suggest(职业建议)、reason(原因分析)、trueOptions(正确选项)、standardAnswer(标准答案解析)、" +
            "recommendedQuestions(推荐的面试题列表)。")
    ResultReportResponse generateReport(
            @V("levelName") String levelName,
            @V("levelDesc") String levelDesc,
            @V("userOptions") String userOptionsJson,
            @V("trueOptions") List<String> trueOptions,
            @V("salary") Integer salary
    );
}