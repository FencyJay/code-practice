package com.codebear.coderpracticebackend.exception;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author fency
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code= errorCode.getCode();
    }


    public BusinessException(String message) {
        super(message);
        this.code = 40000;
    }
}