package com.codebear.coderpracticebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codebear.coderpracticebackend.exception.BusinessException;
import com.codebear.coderpracticebackend.exception.ErrorCode;
import com.codebear.coderpracticebackend.mapper.UserMapper;
import com.codebear.coderpracticebackend.model.entity.User;
import com.codebear.coderpracticebackend.model.dto.user.UserLoginRequest;
import com.codebear.coderpracticebackend.model.dto.user.UserRegisterRequest;
import com.codebear.coderpracticebackend.model.vo.UserVO;
import com.codebear.coderpracticebackend.model.vo.RankingUserVO;
import com.codebear.coderpracticebackend.service.UserService;
import cn.hutool.crypto.digest.DigestUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        user.setUserRole("user");
        user.setIsDelete(0);

        this.save(user);

        return convertToUserVO(user);
    }

    @Override
    public UserVO login(UserLoginRequest loginRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginRequest.getUsername());
        queryWrapper.eq("password", DigestUtil.md5Hex(loginRequest.getPassword()));

        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        httpSession.setAttribute(USER_SESSION_KEY, user);
        return convertToUserVO(user);
    }

    @Override
    public UserVO getCurrentUser() {
        User user = (User)httpSession.getAttribute(USER_SESSION_KEY);
        if (user == null) {
           throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", user.getId());
        user = this.getOne(queryWrapper);
        if (user == null) {
          throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
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
        userVO.setUserRole(user.getUserRole());
        userVO.setCreateTime(user.getCreateTime());

        // 处理头像：如果用户没有设置头像，则提供默认头像
        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            userVO.setAvatar(getDefaultAvatar(user.getId()));
        } else {
            userVO.setAvatar(user.getAvatar());
        }

        return userVO;
    }

    /**
     * 根据用户ID获取默认头像（4选1）
     * @param userId 用户ID
     * @return 默认头像URL
     */
    private String getDefaultAvatar(Long userId) {
        // 根据用户ID的哈希值选择4个默认头像之一
        int avatarIndex = (int)(userId % 4) + 1;
        return "/avatars/avatar" + avatarIndex + ".svg";
    }

    @Override
    public List<RankingUserVO> getUserRanking(Integer limit) {
        // 参数校验（防御性编程，Controller层已经校验过）
        if (limit == null || limit <= 0 || limit > 100) {
            limit = 30; // 默认值
        }

        // 查询未删除用户，按薪资降序排序
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDelete", 0);
        queryWrapper.orderByDesc("salary");
        queryWrapper.last("LIMIT " + limit);

        List<User> userList = this.list(queryWrapper);

        // 如果查询结果为空，返回空列表
        if (userList == null || userList.isEmpty()) {
            return Collections.emptyList();
        }

        // 转换为RankingUserVO并添加排名
        return userList.stream()
                .map(user -> {
                    RankingUserVO rankingUserVO = new RankingUserVO();
                    rankingUserVO.setRank(userList.indexOf(user) + 1);
                    rankingUserVO.setUserId(user.getId());
                    rankingUserVO.setNickname(user.getNickname() != null ? user.getNickname() : "匿名用户");
                    rankingUserVO.setAvatar(user.getAvatar() == null || user.getAvatar().isEmpty() ?
                            getDefaultAvatar(user.getId()) : user.getAvatar());
                    rankingUserVO.setSalary(user.getSalary() != null ? user.getSalary() : 0);
                    rankingUserVO.setUserRole(user.getUserRole() != null ? user.getUserRole() : "user");
                    return rankingUserVO;
                })
                .collect(Collectors.toList());
    }
}