package com.example.service;

import com.example.entity.Goods;
import com.example.mapper.GoodsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品信息业务处理
 */
@Service
public class GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    public void add(Goods goods) {
        goodsMapper.insert(goods);
    }

    public void deleteById(Integer id) {
        goodsMapper.deleteById(id);
    }

    public void updateById(Goods goods) {
        goodsMapper.updateById(goods);
    }

    public void updateRecommend(Goods goods) {
        goodsMapper.updateRecommend(goods);
    }

    public Goods selectById(Integer id) {
        return goodsMapper.selectById(id);
    }

    public List<Goods> selectAll(Goods goods) {
        return goodsMapper.selectAll(goods);
    }

    public PageInfo<Goods> selectPage(Goods goods, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> list = goodsMapper.selectAll(goods);
        return PageInfo.of(list);
    }
}
