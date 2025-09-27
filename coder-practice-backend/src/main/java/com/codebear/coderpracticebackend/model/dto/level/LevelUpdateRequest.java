package com.codebear.coderpracticebackend.model.dto.level;

import lombok.Data;

import java.io.Serializable;

/**
 * 关卡更新请求
 *
 * @author fency
 */
@Data
public class LevelUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关卡ID
     */
    private Long id;

    /**
     * 关卡名称
     */
    private String levelName;

    /**
     * 关卡描述
     */
    private String levelDesc;

    /**
     * 关卡选项（JSON格式）
     */
    private String options;

    /**
     * 难度等级
     */
    private String difficulty;

    /**
     * 目标薪资
     */
    private Integer targetSalary;

    /**
     * 优先级（数值越大优先级越高）
     */
    private Integer priority;
}