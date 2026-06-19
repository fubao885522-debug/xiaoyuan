package com.example.controller;

import com.example.common.Result;
import com.example.entity.Orders;
import com.example.service.OrdersService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单接口
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    /**
     * 立即购买（单个商品直接下单）
     */
    @PostMapping("/buyNow")
    public Result buyNow(@RequestBody Map<String, Object> params) {
        try {
            Integer goodsId = (Integer) params.get("goodsId");
            String goodsName = (String) params.get("goodsName");
            String goodsImg = (String) params.get("goodsImg");
            BigDecimal price = new BigDecimal(params.get("price").toString());
            Integer num = (Integer) params.get("num");
            Integer userId = (Integer) params.get("userId");
            String userName = (String) params.get("userName");
            String deliveryType = (String) params.get("deliveryType");
            String address = (String) params.get("address");
            String deliver = (String) params.get("deliver");

            Orders result = ordersService.buyNow(goodsId, goodsName, goodsImg, price, num, userId, userName, deliveryType, address, deliver);
            return Result.success(result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 创建订单（购物车结算）
     */
    @PostMapping("/create")
    public Result create(@RequestBody Orders orders) {
        try {
            Orders result = ordersService.createFromCart(orders.getUserId(), orders.getUserName());
            return Result.success(result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消订单（退款 + 更新状态）
     */
    @PutMapping("/cancel/{id}")
    public Result cancelOrder(@PathVariable Integer id) {
        try {
            ordersService.cancelOrder(id);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        ordersService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Orders orders) {
        ordersService.updateById(orders);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Orders orders = ordersService.selectById(id);
        return Result.success(orders);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Orders orders) {
        List<Orders> list = ordersService.selectAll(orders);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Orders orders,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Orders> page = ordersService.selectPage(orders, pageNum, pageSize);
        return Result.success(page);
    }

}
