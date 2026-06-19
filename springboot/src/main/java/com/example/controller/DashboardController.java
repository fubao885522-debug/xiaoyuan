package com.example.controller;

import com.example.common.Result;
import com.example.service.DashboardService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 数据统计接口
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @GetMapping("/data")
    public Result getData() {
        Map<String, Object> data = dashboardService.getDashboard();
        return Result.success(data);
    }

    /** 前台首页统计数据 */
    @GetMapping("/frontStats")
    public Result getFrontStats() {
        Map<String, Object> data = dashboardService.getFrontStats();
        return Result.success(data);
    }
}
