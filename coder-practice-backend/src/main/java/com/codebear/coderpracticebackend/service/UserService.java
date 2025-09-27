package com.codebear.coderpracticebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codebear.coderpracticebackend.model.entity.User;
import com.codebear.coderpracticebackend.model.dto.user.UserLoginRequest;
import com.codebear.coderpracticebackend.model.dto.user.UserRegisterRequest;
import com.codebear.coderpracticebackend.model.vo.UserVO;
import com.codebear.coderpracticebackend.model.vo.RankingUserVO;

import java.util.List;

public interface UserService extends IService<User> {

    UserVO register(UserRegisterRequest registerRequest);

    UserVO login(UserLoginRequest loginRequest);

    UserVO getCurrentUser();

    void logout();

    /**
     * 获取用户排行榜
     *
     * @param limit 限制数量
     * @return 排行榜用户列表
     */
    List<RankingUserVO> getUserRanking(Integer limit);
}