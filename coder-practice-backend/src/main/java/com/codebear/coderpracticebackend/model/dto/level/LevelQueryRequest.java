package com.codebear.coderpracticebackend.model.dto.level;

import com.codebear.coderpracticebackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 关卡查询请求
 *
 * @author fency
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LevelQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关卡名称
     */
    private String levelName;

    /**
     * 难度等级
     */
    private String difficulty;

    /**
     * 是否精选
     */
    private Boolean isFeatured;
}