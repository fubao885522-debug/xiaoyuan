package com.example.controller;

import com.example.common.JwtUtils;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
public class WebController {

    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;


    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account ac = null;
        if ("ADMIN".equals(account.getRole())) {
            ac = adminService.login(account);
        } else if ("USER".equals(account.getRole())) {
            ac = userService.login(account);
        }
        // 生成 token
        String token = JwtUtils.createToken(ac.getId(), ac.getRole());
        // 返回 token + 用户信息
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", ac);
        return Result.success(result);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register() {
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        System.out.println("修改密码请求：role=" + account.getRole() + ", username=" + account.getUsername() + ", password=" + account.getPassword() + ", newPassword=" + account.getNewPassword());
        if ("管理员".equals(account.getRole())) {
            adminService.updatePassword(account);
            System.out.println("调用 adminService.updatePassword 完成");
        } else if ("用户".equals(account.getRole())) {
            userService.updatePassword(account);
            System.out.println("调用 userService.updatePassword 完成");
        } else {
            System.out.println("role 不匹配，未执行任何更新！role=[" + account.getRole() + "]");
        }
        return Result.success();
    }

}
