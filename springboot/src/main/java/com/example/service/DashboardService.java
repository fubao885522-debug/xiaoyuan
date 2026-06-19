package com.example.service;

import com.example.mapper.DashboardMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计业务
 */
@Service
public class DashboardService {

    @Resource
    private DashboardMapper dashboardMapper;

    public Map<String, Object> getDashboard() {
        Map<String, Object> result = new HashMap<>();

        // 总览统计
        Map<String, Object> counts = dashboardMapper.countAll();
        result.put("counts", counts);

        // 今日订单和销售额
        result.put("todayOrders", dashboardMapper.countTodayOrders());
        result.put("todaySales", dashboardMapper.sumTodaySales());

        // 近7天销售趋势
        List<Map<String, Object>> sales7 = dashboardMapper.salesByDay7();
        result.put("sales7", sales7);

        // 商品销量 Top5
        List<Map<String, Object>> top5 = dashboardMapper.top5Goods();
        result.put("top5", top5);

        // 订单状态分布
        List<Map<String, Object>> orderStatus = dashboardMapper.orderStatusCount();
        result.put("orderStatus", orderStatus);

        // 按分类统计销售额
        List<Map<String, Object>> salesByCategory = dashboardMapper.salesByCategory();
        result.put("salesByCategory", salesByCategory);

        return result;
    }

    /** 前台首页统计：销售总额、今日销售、商品总数、注册数量 */
    public Map<String, Object> getFrontStats() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalSales", dashboardMapper.sumTotalSales());
        result.put("todaySales", dashboardMapper.sumTodaySales());
        result.put("goodsCount", dashboardMapper.countAll().get("goodsCount"));
        result.put("userCount", dashboardMapper.countUsers());
        return result;
    }
}
