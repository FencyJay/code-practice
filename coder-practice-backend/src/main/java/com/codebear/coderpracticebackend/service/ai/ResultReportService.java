package com.codebear.coderpracticebackend.service.ai;

import com.codebear.coderpracticebackend.service.ai.dto.ResultReportRequest;
import com.codebear.coderpracticebackend.service.ai.dto.ResultReportResponse;

/**
 * AI结果报告生成服务接口
 */
public interface ResultReportService {

    /**
     * 根据用户答题情况生成结果报告
     * @param request 包含关卡信息和用户答案的请求
     * @return 生成的结果报告
     */
    ResultReportResponse generateReport(ResultReportRequest request);
}