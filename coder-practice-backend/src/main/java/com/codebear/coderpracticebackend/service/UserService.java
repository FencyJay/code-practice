package com.codebear.coderpracticebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codebear.coderpracticebackend.model.entity.User;
import com.codebear.coderpracticebackend.model.dto.user.UserLoginRequest;
import com.codebear.coderpracticebackend.model.dto.user.UserRegisterRequest;
import com.codebear.coderpracticebackend.model.vo.UserVO;

public interface UserService extends IService<User> {

    UserVO register(UserRegisterRequest registerRequest);

    UserVO login(UserLoginRequest loginRequest);

    UserVO getCurrentUser();

    void logout();
}