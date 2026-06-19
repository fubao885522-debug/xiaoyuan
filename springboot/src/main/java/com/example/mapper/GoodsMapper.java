package com.example.mapper;

import com.example.entity.Goods;

import java.util.List;

/**
 * 操作goods相关数据接口
 */
public interface GoodsMapper {

    int insert(Goods goods);
    int deleteById(Integer id);
    int updateById(Goods goods);
    int updateRecommend(Goods goods);
    Goods selectById(Integer id);
    List<Goods> selectAll(Goods goods);

}
