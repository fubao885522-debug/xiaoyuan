package com.example.controller;

import com.example.common.Result;
import com.example.entity.Goods;
import com.example.service.GoodsService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品信息前端操作接口
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @PostMapping("/add")
    public Result add(@RequestBody Goods goods) {
        goodsService.add(goods);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        goodsService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Goods goods) {
        goodsService.updateById(goods);
        return Result.success();
    }

    @PutMapping("/updateRecommend")
    public Result updateRecommend(@RequestBody Goods goods) {
        goodsService.updateRecommend(goods);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Goods goods = goodsService.selectById(id);
        return Result.success(goods);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Goods goods) {
        List<Goods> list = goodsService.selectAll(goods);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Goods goods,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Goods> page = goodsService.selectPage(goods, pageNum, pageSize);
        return Result.success(page);
    }

}
