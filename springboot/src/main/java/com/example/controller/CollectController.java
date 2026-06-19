package com.example.controller;

import com.example.common.Result;
import com.example.entity.Collect;
import com.example.service.CollectService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收藏操作接口
 */
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    @PostMapping("/add")
    public Result add(@RequestBody Collect collect) {
        // 检查是否已收藏
        Collect exist = collectService.selectByUserIdAndGoodsId(collect);
        if (exist != null) {
            return Result.error("已收藏过该商品");
        }
        collectService.add(collect);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        collectService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Collect collect) {
        collectService.updateById(collect);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Collect collect = collectService.selectById(id);
        return Result.success(collect);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Collect collect) {
        List<Collect> list = collectService.selectAll(collect);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Collect collect,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Collect> page = collectService.selectPage(collect, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/check")
    public Result check(Collect collect) {
        Collect exist = collectService.selectByUserIdAndGoodsId(collect);
        return Result.success(exist);
    }

}
