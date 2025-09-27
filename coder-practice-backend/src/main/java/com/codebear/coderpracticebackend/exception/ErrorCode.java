package com.codebear.coderpracticebackend.exception;

import lombok.Getter;

/**
 * 错误码
 *
 * @author fency
 */
@Getter
public enum ErrorCode {

    SUCCESS(0, "操作成功"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40300, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    SYSTEM_ERROR(50000, "系统内部异常");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}