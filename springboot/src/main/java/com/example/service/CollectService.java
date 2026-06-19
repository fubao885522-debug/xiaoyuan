package com.example.service;

import com.example.entity.Collect;
import com.example.mapper.CollectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收藏业务处理
 */
@Service
public class CollectService {

    @Resource
    private CollectMapper collectMapper;

    public void add(Collect collect) {
        collectMapper.insert(collect);
    }

    public void deleteById(Integer id) {
        collectMapper.deleteById(id);
    }

    public void updateById(Collect collect) {
        collectMapper.updateById(collect);
    }

    public Collect selectById(Integer id) {
        return collectMapper.selectById(id);
    }

    public List<Collect> selectAll(Collect collect) {
        return collectMapper.selectAll(collect);
    }

    public PageInfo<Collect> selectPage(Collect collect, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> list = collectMapper.selectAll(collect);
        return PageInfo.of(list);
    }

    public Collect selectByUserIdAndGoodsId(Collect collect) {
        return collectMapper.selectByUserIdAndGoodsId(collect);
    }

}
