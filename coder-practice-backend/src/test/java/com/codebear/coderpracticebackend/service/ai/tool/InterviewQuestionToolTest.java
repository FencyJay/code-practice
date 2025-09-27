package com.codebear.coderpracticebackend.service.ai.tool;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 面试题工具测试类
 */
@SpringBootTest
public class InterviewQuestionToolTest {

    @Autowired
    private InterviewQuestionTool interviewQuestionTool;

    @Test
    public void testSearchInterviewQuestions() {
        // 测试搜索Java相关的面试题
        List<InterviewQuestionTool.InterviewQuestion> questions = interviewQuestionTool.searchInterviewQuestions("Java", 5);

        assertNotNull(questions);
        assertFalse(questions.isEmpty());

        // 打印搜索结果
        System.out.println("搜索到 " + questions.size() + " 个面试题：");
        for (InterviewQuestionTool.InterviewQuestion question : questions) {
            System.out.println("题目: " + question.title());
            System.out.println("难度: " + question.difficulty());
            System.out.println("标签: " + question.tags());
            System.out.println("链接: " + question.url());
            System.out.println("-------------------");
        }
    }

    @Test
    public void testSearchWithEmptyQuery() {
        // 测试空查询
        List<InterviewQuestionTool.InterviewQuestion> questions = interviewQuestionTool.searchInterviewQuestions("", 5);
        assertNotNull(questions);
        assertTrue(questions.isEmpty());
    }

    @Test
    public void testSearchWithMaxResults() {
        // 测试限制结果数量
        List<InterviewQuestionTool.InterviewQuestion> questions = interviewQuestionTool.searchInterviewQuestions("Spring Boot", 3);
        assertNotNull(questions);
        assertTrue(questions.size() <= 3);
    }
}