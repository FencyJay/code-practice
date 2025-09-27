package com.codebear.coderpracticebackend.utils;

import com.codebear.coderpracticebackend.exception.BusinessException;
import com.codebear.coderpracticebackend.exception.ErrorCode;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 参数校验工具类
 * 提供通用的参数校验方法
 *
 * @author fency
 */
public class ValidationUtils {

    // 邮箱正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // 用户名正则表达式（字母开头，允许字母数字下划线，长度4-20）
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[a-zA-Z][a-zA-Z0-9_]{3,19}$"
    );

    // 密码正则表达式（至少8位，包含字母和数字）
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$"
    );

    /**
     * 校验参数是否为null
     *
     * @param param     参数
     * @param paramName 参数名称
     */
    public static void notNull(Object param, String paramName) {
        if (param == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, paramName + "不能为空");
        }
    }

    /**
     * 校验字符串是否为空或null
     *
     * @param param     参数
     * @param paramName 参数名称
     */
    public static void notEmpty(String param, String paramName) {
        if (param == null || param.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, paramName + "不能为空");
        }
    }

    /**
     * 校验集合是否为空或null
     *
     * @param param     参数
     * @param paramName 参数名称
     */
    public static void notEmpty(Collection<?> param, String paramName) {
        if (param == null || param.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, paramName + "不能为空");
        }
    }

    /**
     * 校验数组是否为空或null
     *
     * @param param     参数
     * @param paramName 参数名称
     */
    public static void notEmpty(Object[] param, String paramName) {
        if (param == null || param.length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, paramName + "不能为空");
        }
    }

    /**
     * 校验Map是否为空或null
     *
     * @param param     参数
     * @param paramName 参数名称
     */
    public static void notEmpty(Map<?, ?> param, String paramName) {
        if (param == null || param.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, paramName + "不能为空");
        }
    }

    /**
     * 校验数字是否在指定范围内
     *
     * @param number    数字
     * @param min       最小值
     * @param max       最大值
     * @param paramName 参数名称
     */
    public static void range(Integer number, int min, int max, String paramName) {
        notNull(number, paramName);
        if (number < min || number > max) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, paramName + "必须在" + min + "-" + max + "之间");
        }
    }

    /**
     * 校验数字是否在指定范围内（Long类型）
     *
     * @param number    数字
     * @param min       最小值
     * @param max       最大值
     * @param paramName 参数名称
     */
    public static void range(Long number, long min, long max, String paramName) {
        notNull(number, paramName);
        if (number < min || number > max) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, paramName + "必须在" + min + "-" + max + "之间");
        }
    }

    /**
     * 校验字符串长度
     *
     * @param str       字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @param paramName 参数名称
     */
    public static void length(String str, int minLength, int maxLength, String paramName) {
        notNull(str, paramName);
        if (str.length() < minLength || str.length() > maxLength) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, paramName + "长度必须在" + minLength + "-" + maxLength + "之间");
        }
    }

    /**
     * 校验邮箱格式
     *
     * @param email     邮箱
     * @param paramName 参数名称
     */
    public static void email(String email, String paramName) {
        notEmpty(email, paramName);
//        if (!EMAIL_PATTERN.matcher(email).matches()) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, paramName + "格式不正确");
//        }
    }

    /**
     * 校验用户名格式
     *
     * @param username  用户名
     * @param paramName 参数名称
     */
    public static void username(String username, String paramName) {
        notEmpty(username, paramName);

//        if (!USERNAME_PATTERN.matcher(username).matches()) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR,
//                    paramName + "必须以字母开头，只能包含字母、数字、下划线，长度4-20位");
//        }
    }

    /**
     * 校验密码格式
     *
     * @param password  密码
     * @param paramName 参数名称
     */
    public static void password(String password, String paramName) {
        notEmpty(password, paramName);
//        if (!PASSWORD_PATTERN.matcher(password).matches()) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR,
//                    paramName + "至少8位，必须包含字母和数字");
//        }
    }

    /**
     * 校验ID是否为正数
     *
     * @param id        ID
     * @param paramName 参数名称
     */
    public static void positiveId(Long id, String paramName) {
        notNull(id, paramName);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, paramName + "必须为正数");
        }
    }

    /**
     * 校验ID是否为正数（Integer类型）
     *
     * @param id        ID
     * @param paramName 参数名称
     */
    public static void positiveId(Integer id, String paramName) {
        notNull(id, paramName);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, paramName + "必须为正数");
        }
    }

    /**
     * 校验分页参数
     *
     * @param current   当前页
     * @param pageSize 每页大小
     */
    public static void pagination(Integer current, Integer pageSize) {
        range(current, 1, 1000, "页码");
        range(pageSize, 1, 100, "每页大小");
    }

    /**
     * 校验枚举值是否在允许的范围内
     *
     * @param value     值
     * @param allowed   允许的值数组
     * @param paramName 参数名称
     */
    public static void enumValue(String value, String[] allowed, String paramName) {
        if (value != null && !value.isEmpty()) {
            for (String allowedValue : allowed) {
                if (allowedValue.equals(value)) {
                    return;
                }
            }
            throw new BusinessException(ErrorCode.PARAMS_ERROR, paramName + "无效");
        }
    }

    /**
     * 校验薪资范围（防止异常值）
     *
     * @param salary    薪资
     * @param paramName 参数名称
     */
    public static void salary(Integer salary, String paramName) {
        range(salary, 0, 1000000, paramName);
    }
}