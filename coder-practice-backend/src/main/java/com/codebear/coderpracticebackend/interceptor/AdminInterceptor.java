package com.codebear.coderpracticebackend.interceptor;

import com.codebear.coderpracticebackend.model.entity.User;
import com.codebear.coderpracticebackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 管理员权限拦截器
 *
 * @author fency
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从session中获取用户信息
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":40100,\"message\":\"未登录\",\"data\":null}");
            return false;
        }

        // 检查用户角色是否为管理员
        if (!"admin".equals(user.getUserRole())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"code\":40300,\"message\":\"无管理员权限\",\"data\":null}");
            return false;
        }

        return true;
    }
}