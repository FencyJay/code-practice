package com.codebear.coderpracticebackend.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {

    private Long id;

    private String username;

    private String nickname;

    private Integer salary;

    private LocalDateTime createTime;
}