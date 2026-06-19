package com.example.common;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

/**
 * 登录拦截器：校验 token 和角色
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS 预检请求放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getRequestURI();

        // 不需要拦截的路径
        if (path.startsWith("/login") || path.startsWith("/register") || path.equals("/") || path.startsWith("/files/")) {
            return true;
        }

        // 获取 token
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            sendError(response, "401", "请先登录");
            return false;
        }

        // 校验 token
        if (!JwtUtils.verify(token)) {
            sendError(response, "401", "token已过期，请重新登录");
            return false;
        }

        // 所有接口登录后都可访问，页面级权限由前端路由守卫控制
        return true;
    }

    private void sendError(HttpServletResponse response, String code, String msg) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(JSONUtil.toJsonStr(Result.error(code, msg)));
        out.flush();
    }
}
