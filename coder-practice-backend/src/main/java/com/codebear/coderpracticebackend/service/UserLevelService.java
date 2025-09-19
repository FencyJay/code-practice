package com.codebear.coderpracticebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codebear.coderpracticebackend.common.PageRequest;
import com.codebear.coderpracticebackend.common.PageResult;
import com.codebear.coderpracticebackend.model.entity.UserLevel;
import com.codebear.coderpracticebackend.model.dto.user.UserSubmitAnswerRequest;

public interface UserLevelService extends IService<UserLevel> {

    PageResult<UserLevel> getUserLevelHistory(Long userId, PageRequest pageRequest);

    UserLevel submitAnswer(Long userId, UserSubmitAnswerRequest submitRequest);
}