package com.codebear.coderpracticebackend.exception;

import com.codebear.coderpracticebackend.common.Result;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.swagger.v3.oas.annotations.Hidden;

/**
 * 全局异常处理器
 *
 * @author fency
 */
@Slf4j
@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException: {}", e.getMessage());
        // sentry 捕获异常
        Sentry.captureException(e);
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException: ", e);
        // sentry 捕获异常
        Sentry.captureException(e);
        return Result.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统错误");
    }
}