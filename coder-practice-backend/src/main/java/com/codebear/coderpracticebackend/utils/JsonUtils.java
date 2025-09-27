package com.codebear.coderpracticebackend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;

/**
 * JSON工具类
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将对象转换为JSON字符串
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JSON序列化失败", e);
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    /**
     * 将JSON字符串转换为指定类型的对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON反序列化失败", e);
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }

    /**
     * 将JSON字符串转换为指定类型的对象列表
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("JSON反序列化失败", e);
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }
}