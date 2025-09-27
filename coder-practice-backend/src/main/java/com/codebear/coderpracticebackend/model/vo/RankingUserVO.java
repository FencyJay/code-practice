package com.codebear.coderpracticebackend.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 排行榜用户视图对象
 *
 * @author fency
 */
@Data
public class RankingUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 排名
     */
    private Integer rank;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 薪资
     */
    private Integer salary;

    /**
     * 用户角色
     */
    private String userRole;
}