package com.example.common;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
public class JwtUtils {

    private static final String SECRET = "code2026-secret-key";

    /**
     * 生成 token
     */
    public static String createToken(Integer userId, String role) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("role", role);
        // 设置过期时间 24 小时
        payload.put("exp", System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        return JWTUtil.createToken(payload, SECRET.getBytes());
    }

    /**
     * 校验 token 是否有效
     */
    public static boolean verify(String token) {
        try {
            return JWTUtil.verify(token, SECRET.getBytes());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从 token 中获取用户信息
     */
    public static JWT parseToken(String token) {
        try {
            return JWTUtil.parseToken(token);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 token 中获取用户 ID
     */
    public static Integer getUserId(String token) {
        JWT jwt = parseToken(token);
        if (jwt == null) return null;
        return Integer.parseInt(jwt.getPayload("userId").toString());
    }

    /**
     * 从 token 中获取角色
     */
    public static String getRole(String token) {
        JWT jwt = parseToken(token);
        if (jwt == null) return null;
        Object role = jwt.getPayload("role");
        return role != null ? role.toString() : null;
    }
}
