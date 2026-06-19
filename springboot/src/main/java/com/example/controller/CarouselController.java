package com.example.controller;

import com.example.common.Result;
import com.example.entity.Carousel;
import com.example.service.CarouselService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 轮播图信息前端操作接口
 */
@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Resource
    private CarouselService carouselService;

    @PostMapping("/add")
    public Result add(@RequestBody Carousel carousel) {
        carouselService.add(carousel);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        carouselService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Carousel carousel) {
        carouselService.updateById(carousel);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Carousel carousel = carouselService.selectById(id);
        return Result.success(carousel);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Carousel carousel) {
        List<Carousel> list = carouselService.selectAll(carousel);
        return Result.success(list);
    }
}
