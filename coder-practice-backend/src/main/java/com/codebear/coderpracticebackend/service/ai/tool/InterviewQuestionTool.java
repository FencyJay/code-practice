package com.codebear.coderpracticebackend.service.ai.tool;

import cn.hutool.core.util.StrUtil;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 面试题搜索工具
 * 从面试鸭网站搜索相关面试题
 */
@Slf4j
@org.springframework.stereotype.Component
public class InterviewQuestionTool {

    private static final String BASE_URL = "https://www.mianshiya.com";
    private static final String SEARCH_URL = BASE_URL + "/search/all?searchText=";

    /**
     * 面试题信息
     */
    public record InterviewQuestion(
        String title,
        String url,
        String difficulty,
        String company,
        String tags
    ) {}

    /**
     * 搜索面试题
     *
     * @param query 搜索关键词
     * @param maxResults 最大返回结果数量
     * @return 面试题列表
     */
    @Tool("根据关键词搜索面试题，从面试鸭网站获取相关题目")
    public List<InterviewQuestion> searchInterviewQuestions(String query, Integer maxResults) {

        log.info("开始搜索面试题，关键词：{}", query);

        if (StrUtil.isBlank(query)) {
            log.warn("搜索关键词为空，返回空列表");
            return new ArrayList<>();
        }

        if (maxResults == null || maxResults <= 0) {
            maxResults = 5;
        }

        if (maxResults > 10) {
            maxResults = 10; // 限制最大返回数量
        }

        try {
            List<InterviewQuestion> questions = new ArrayList<>();
            String searchUrl = SEARCH_URL + java.net.URLEncoder.encode(query, "UTF-8");

            log.debug("搜索URL: {}", searchUrl);

            Document doc = Jsoup.connect(searchUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(15000)
                    .followRedirects(true)
                    .get();

            // 查找题目列表容器 - 根据实际HTML结构，题目在表格的tbody中的tr里
            Elements questionElements = doc.select(".ant-table-tbody tr");

            log.debug("找到 {} 个题目元素", questionElements.size());

            for (Element element : questionElements) {
                if (questions.size() >= maxResults) {
                    break;
                }

                try {
                    InterviewQuestion question = parseQuestion(element);
                    if (question != null) {
                        questions.add(question);
                        log.debug("成功解析题目: {}", question.title());
                    }
                } catch (Exception e) {
                    log.warn("解析面试题时出错：{}", e.getMessage());
                    continue;
                }
            }

            log.info("成功搜索到 {} 个面试题", questions.size());
            return questions;

        } catch (IOException e) {
            log.error("搜索面试题时发生IO异常：{}，URL: {}", e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("搜索面试题时发生异常：{}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 解析单个题目元素
     */
    private InterviewQuestion parseQuestion(Element element) {
        try {
            // 从实际HTML结构看，题目在表格行中，结构为：
            // tr -> td[标记] -> td[题目链接] -> td[难度] -> td[标签]

            // 提取标题和链接 - 题目在第二个td单元格中
            Elements titleCells = element.select("td");

            if (titleCells.size() < 2) {
                log.warn("题目行结构不正确，单元格数量不足: {}", element.html());
                return null;
            }

            // 第二个td单元格包含题目链接
            Element titleCell = titleCells.get(1);
            Element titleLink = titleCell.select("a").first();

            String title;
            String url;

            if (titleLink != null) {
                // 有链接的情况
                title = titleLink.text().trim();
                String href = titleLink.attr("href");
                url = href.startsWith("/") ? BASE_URL + href : href;
            } else {
                // 没有链接的情况，可能是隐藏题目或者VIP题目
                title = titleCell.text().trim();
                url = ""; // 空链接表示这是隐藏题目
                log.debug("题目没有链接，可能是隐藏或VIP题目: {}", title);
            }

            // 如果标题为空，则跳过这个题目
            if (StrUtil.isBlank(title)) {
                log.warn("题目标题为空，跳过此题目");
                return null;
            }

            // 提取难度 - 在第三个td单元格中
            String difficulty = "未知";
            if (titleCells.size() >= 3) {
                Element difficultyCell = titleCells.get(2);
                difficulty = difficultyCell.text().trim();
            }

            // 提取标签 - 在第四个td单元格中
            String tags = "";
            if (titleCells.size() >= 4) {
                Element tagsCell = titleCells.get(3);
                // 提取所有标签链接的文本
                Elements tagLinks = tagsCell.select("a");
                StringBuilder tagsBuilder = new StringBuilder();
                for (Element tagLink : tagLinks) {
                    if (tagsBuilder.length() > 0) {
                        tagsBuilder.append(", ");
                    }
                    tagsBuilder.append(tagLink.text().trim());
                }
                tags = tagsBuilder.toString();
            }

            return new InterviewQuestion(title, url, difficulty, "", tags);

        } catch (Exception e) {
            log.error("解析题目元素时发生错误：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取面试题详情
     *
     * @param questionUrl 题目URL
     * @return 题目详情
     */
    @Tool("获取面试题的详细内容和答案")
    public String getInterviewQuestionDetail(String questionUrl) {
        log.info("获取面试题详情：{}", questionUrl);

        if (StrUtil.isBlank(questionUrl) || !questionUrl.startsWith(BASE_URL)) {
            log.warn("无效的题目URL：{}", questionUrl);
            return "无效的题目URL";
        }

        try {
            Document doc = Jsoup.connect(questionUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(15000)
                    .followRedirects(true)
                    .get();

            // 提取题目内容
            Element contentElement = doc.select(".question-content").first();
            String content = contentElement != null ? contentElement.text().trim() : "无法获取题目内容";

            // 提取答案
            Element answerElement = doc.select(".answer-content").first();
            String answer = answerElement != null ? answerElement.text().trim() : "暂无答案";

            return String.format("题目：%s\n\n答案：%s", content, answer);

        } catch (IOException e) {
            log.error("获取面试题详情时发生IO异常：{}", e.getMessage());
            return "获取题目详情失败";
        } catch (Exception e) {
            log.error("获取面试题详情时发生异常：{}", e.getMessage());
            return "获取题目详情失败";
        }
    }

    /**
     * 简单的测试工具方法
     *
     * @param message 测试消息
     * @return 响应消息
     */
    @Tool("测试工具调用是否正常工作")
    public String testToolCall(String message) {
        log.info("测试工具调用，消息：{}", message);
        return "工具调用正常！收到消息：" + message;
    }
}