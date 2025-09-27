package com.codebear.coderpracticebackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("user_level")
public class UserLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("userId")
    private Long userId;

    @TableField("levelId")
    private Long levelId;

    @TableField("userOptions")
    private String userOptions;

    @TableField("score")
    private Integer score;

    @TableField("comment")
    private String comment;

    @TableField("salaryChange")
    private Integer salaryChange;

    @TableField("suggest")
    private String suggest;

    @TableField("reason")
    private String reason;

    @TableField("trueOptions")
    private String trueOptions;

    @TableField("standardAnswer")
    private String standardAnswer;

    @TableField("recommendedQuestions")
    private String recommendedQuestions;

    @TableField(value = "createTime", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("isDelete")
    private Integer isDelete;
}