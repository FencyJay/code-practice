package com.codebear.coderpracticebackend.model.dto.level;

import lombok.Data;

import java.io.Serializable;

/**
 * 设置精选关卡请求
 *
 * @author fency
 */
@Data
public class SetFeaturedRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关卡ID
     */
    private Long id;

    /**
     * 优先级（数值越大优先级越高）
     */
    private Integer priority;
}