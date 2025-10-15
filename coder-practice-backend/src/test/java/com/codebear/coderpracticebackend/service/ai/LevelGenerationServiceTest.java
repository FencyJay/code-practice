package com.codebear.coderpracticebackend.service.ai;

import com.codebear.coderpracticebackend.service.ai.dto.LevelGenerationResponse;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LevelGenerationServiceTest {

    @Resource
    private LevelGenerationService levelGenerationService;

    @Test
    void generateLevel() {
        LevelGenerationResponse levelInfo = levelGenerationService.generateLevel(8000, "Java");
        Assertions.assertNotNull(levelInfo);
    }
}