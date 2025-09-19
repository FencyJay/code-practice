package com.codebear.coderpracticebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codebear.coderpracticebackend.mapper.UserMapper;
import com.codebear.coderpracticebackend.model.entity.User;
import com.codebear.coderpracticebackend.model.dto.user.UserLoginRequest;
import com.codebear.coderpracticebackend.model.dto.user.UserRegisterRequest;
import com.codebear.coderpracticebackend.model.vo.UserVO;
import com.codebear.coderpracticebackend.service.UserService;
import cn.hutool.crypto.digest.DigestUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String USER_SESSION_KEY = "user";

    @Autowired
    private HttpSession httpSession;

    @Override
    public UserVO register(UserRegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", registerRequest.getUsername());
        queryWrapper.eq("isDelete", 0);

        User existUser = this.getOne(queryWrapper);
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(DigestUtil.md5Hex(registerRequest.getPassword()));
        user.setNickname(registerRequest.getNickname());
        user.setSalary(10000);
        user.setIsDelete(0);

        this.save(user);

        return convertToUserVO(user);
    }

    @Override
    public UserVO login(UserLoginRequest loginRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginRequest.getUsername());
        queryWrapper.eq("password", DigestUtil.md5Hex(loginRequest.getPassword()));
        queryWrapper.eq("isDelete", 0);

        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        httpSession.setAttribute(USER_SESSION_KEY, user);
        return convertToUserVO(user);
    }

    @Override
    public UserVO getCurrentUser() {
        User user = (User) httpSession.getAttribute(USER_SESSION_KEY);
        if (user == null) {
            throw new RuntimeException("用户未登录");
        }
        return convertToUserVO(user);
    }

    @Override
    public void logout() {
        User user = (User) httpSession.getAttribute(USER_SESSION_KEY);
        if (user == null) {
            throw new RuntimeException("用户未登录");
        }
        httpSession.removeAttribute(USER_SESSION_KEY);
        httpSession.invalidate();
    }

    private UserVO convertToUserVO(User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setSalary(user.getSalary());
        userVO.setCreateTime(user.getCreateTime());
        return userVO;
    }
}