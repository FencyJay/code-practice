package com.codebear.coderpracticebackend.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {

    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private Integer salary;

    private String userRole;

    private LocalDateTime createTime;
}