package com.codebear.coderpracticebackend.service.ai.impl;

import com.codebear.coderpracticebackend.service.ai.ResultReportService;
import com.codebear.coderpracticebackend.service.ai.dto.ResultReportRequest;
import com.codebear.coderpracticebackend.service.ai.dto.ResultReportResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * AI结果报告生成服务实现
 */
@Slf4j
@Service
public class ResultReportServiceImpl implements ResultReportService {

    @Override
    public ResultReportResponse generateReport(ResultReportRequest request) {
        log.info("AI生成结果报告开始，关卡: {}, 用户薪资: {}", request.getLevelName(), request.getSalary());

        try {
            // 计算得分
            int score = calculateScore(request);

            // 生成报告
            return generateReportResponse(request, score);

        } catch (Exception e) {
            log.error("AI生成结果报告失败", e);
            // 返回默认报告
            return generateDefaultResponse(request);
        }
    }

    /**
     * 计算得分
     */
    private int calculateScore(ResultReportRequest request) {
        if (request.getUserOptions() == null || request.getUserOptions().isEmpty()) {
            return 0;
        }

        int correctCount = 0;
        int totalCount = request.getUserOptions().size();

        for (ResultReportRequest.Option userOption : request.getUserOptions()) {
            if (Boolean.TRUE.equals(userOption.getTrueAnswer())) {
                correctCount++;
            }
        }

        return totalCount > 0 ? (correctCount * 100 / totalCount) : 0;
    }

    /**
     * 生成报告响应
     */
    private ResultReportResponse generateReportResponse(ResultReportRequest request, int score) {
        ResultReportResponse response = new ResultReportResponse();
        response.setScore(score);
        response.setTrueOptions(request.getTrueOptions());

        // 根据得分生成评价和建议
        if (score >= 90) {
            response.setComment("太棒了！你的技术实力令人印象深刻！");
            response.setSalaryChange(calculateSalaryChange(request.getSalary(), 3000, 5000));
            response.setSuggest("建议投递\"阿巴阿巴\"、\"企鹅大王\"等一线互联网公司");
            response.setReason("你几乎答对了所有问题，展现了扎实的技术功底和丰富的经验。");
        } else if (score >= 70) {
            response.setComment("不错！继续保持！");
            response.setSalaryChange(calculateSalaryChange(request.getSalary(), 1000, 3000));
            response.setSuggest("建议投递中型互联网公司或知名企业");
            response.setReason("你答对了大部分问题，技术基础良好，还有提升空间。");
        } else if (score >= 50) {
            response.setComment("还需努力，加油！");
            response.setSalaryChange(0);
            response.setSuggest("建议继续学习，积累更多项目经验");
            response.setReason("你的答对了一半，基础还行，但需要更深入的学习。");
        } else if (score >= 30) {
            response.setComment("需要加强学习啊！");
            response.setSalaryChange(calculateSalaryChange(request.getSalary(), -1000, -500));
            response.setSuggest("建议从基础开始，系统学习技术知识");
            response.setReason("答对的题目较少，需要在基础知识上多下功夫。");
        } else {
            response.setComment("建议重新学习基础！");
            response.setSalaryChange(calculateSalaryChange(request.getSalary(), -2000, -1000));
            response.setSuggest("建议参加培训或找导师指导");
            response.setReason("答对题目很少，需要系统性地重新学习。");
        }

        // 生成标准答案解析
        response.setStandardAnswer(generateStandardAnswer(request));

        return response;
    }

    /**
     * 计算薪资变化
     */
    private int calculateSalaryChange(int currentSalary, int minChange, int maxChange) {
        // 根据当前薪资范围调整变化幅度
        if (currentSalary < 10000) {
            return (int) (Math.random() * (maxChange - minChange + 1)) + minChange;
        } else if (currentSalary < 20000) {
            return (int) (Math.random() * (maxChange - minChange + 1)) + minChange;
        } else if (currentSalary < 35000) {
            return (int) (Math.random() * (maxChange - minChange + 1)) + minChange;
        } else {
            return (int) (Math.random() * (maxChange - minChange + 1)) + minChange;
        }
    }

    /**
     * 生成标准答案解析
     */
    private String generateStandardAnswer(ResultReportRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("## 关卡分析\n\n");
        sb.append("### 需求概述\n");
        sb.append(request.getLevelDesc()).append("\n\n");

        sb.append("### 正确技术方案\n");
        sb.append("在实现此类需求时，推荐使用以下技术栈：\n\n");

        if (request.getTrueOptions() != null) {
            for (String option : request.getTrueOptions()) {
                sb.append("- **").append(option).append("**：");
                sb.append(getOptionDescription(option)).append("\n");
            }
        }

        sb.append("\n### 实施建议\n");
        sb.append("1. **技术选型**：根据团队技术栈和项目需求选择合适的技术\n");
        sb.append("2. **架构设计**：采用分层架构，确保代码的可维护性和扩展性\n");
        sb.append("3. **开发流程**：遵循敏捷开发流程，确保项目质量和进度\n");
        sb.append("4. **测试策略**：编写单元测试和集成测试，确保代码质量\n");
        sb.append("5. **部署运维**：使用容器化技术，简化部署和运维流程\n");

        return sb.toString();
    }

    /**
     * 获取选项描述
     */
    private String getOptionDescription(String optionName) {
        switch (optionName) {
            case "Spring Boot":
                return "简化Spring应用开发的框架，提供自动配置和开箱即用的功能";
            case "MySQL":
                return "流行的关系型数据库，适合大多数业务系统的数据存储需求";
            case "Redis":
                return "高性能的内存数据库，常用于缓存和会话管理";
            case "Docker":
                return "容器化技术，简化应用部署和环境管理";
            case "Git":
                return "分布式版本控制系统，团队协作开发的标准工具";
            case "Vue.js":
                return "渐进式JavaScript框架，用于构建用户界面";
            case "Java":
                return "成熟的企业级编程语言，拥有丰富的生态系统";
            default:
                return "重要的技术组件，在项目开发中发挥关键作用";
        }
    }

    /**
     * 生成默认响应
     */
    private ResultReportResponse generateDefaultResponse(ResultReportRequest request) {
        ResultReportResponse response = new ResultReportResponse();
        response.setScore(50);
        response.setComment("系统默认评价");
        response.setSalaryChange(0);
        response.setSuggest("继续学习，提升技能");
        response.setReason("由于系统繁忙，使用默认评分方式");
        response.setTrueOptions(request.getTrueOptions());
        response.setStandardAnswer("系统正在处理您的答案，请稍后查看详细分析。");
        return response;
    }
}