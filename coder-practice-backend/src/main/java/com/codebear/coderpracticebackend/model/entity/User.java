package com.codebear.coderpracticebackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("nickname")
    private String nickname;

    @TableField("avatar")
    private String avatar;

    @TableField("salary")
    private Integer salary;

    @TableField("userRole")
    private String userRole;

    @TableField(value = "createTime", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("isDelete")
    private Integer isDelete;
}