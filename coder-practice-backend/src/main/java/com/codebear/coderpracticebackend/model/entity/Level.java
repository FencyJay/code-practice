package com.codebear.coderpracticebackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("level")
public class Level implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("levelName")
    private String levelName;

    @TableField("levelDesc")
    private String levelDesc;

    @TableField("options")
    private String options;

    @TableField("difficulty")
    private String difficulty;

    @TableField("targetSalary")
    private Integer targetSalary;

    @TableField(value = "createTime", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("isDelete")
    private Integer isDelete;

    @TableField("priority")
    private Integer priority;

    @TableField("direction")
    private String direction;
}