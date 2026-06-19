package com.example.controller;

import com.example.common.Result;
import com.example.entity.Cart;
import com.example.service.CartService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车操作接口
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @PostMapping("/add")
    public Result add(@RequestBody Cart cart) {
        cartService.add(cart);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        cartService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/clear/{userId}")
    public Result clearByUserId(@PathVariable Integer userId) {
        cartService.deleteByUserId(userId);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Cart cart) {
        cartService.updateById(cart);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Cart cart = cartService.selectById(id);
        return Result.success(cart);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Cart cart) {
        List<Cart> list = cartService.selectAll(cart);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Cart cart,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Cart> page = cartService.selectPage(cart, pageNum, pageSize);
        return Result.success(page);
    }

}
