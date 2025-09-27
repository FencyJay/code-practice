package com.codebear.coderpracticebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codebear.coderpracticebackend.common.PageRequest;
import com.codebear.coderpracticebackend.common.PageResult;
import com.codebear.coderpracticebackend.model.entity.UserLevel;
import com.codebear.coderpracticebackend.model.dto.user.UserSubmitAnswerRequest;

public interface UserLevelService extends IService<UserLevel> {

    PageResult<UserLevel> getUserLevelHistory(Long userId, PageRequest pageRequest);

    UserLevel submitAnswer(Long userId, UserSubmitAnswerRequest submitRequest);

    /**
     * 根据ID获取用户闯关结果详情
     * @param id 用户闯关记录ID
     * @param currentUserId 当前用户ID（用于权限验证）
     * @return 用户闯关结果详情
     */
    UserLevel getUserLevelResult(Long id, Long currentUserId);
}