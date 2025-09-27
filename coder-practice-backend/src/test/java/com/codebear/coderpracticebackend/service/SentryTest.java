package com.codebear.coderpracticebackend.service;

import io.sentry.Sentry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SentryTest {

    @Test
    public void testSentry() {
        System.out.println("Sentry test start...");
        try {
            throw new Exception("This is a test.");

        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }
}
