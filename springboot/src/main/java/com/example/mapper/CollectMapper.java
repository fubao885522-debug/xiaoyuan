package com.example.mapper;

import com.example.entity.Collect;
import java.util.List;

/**
 * 收藏数据访问
 */
public interface CollectMapper {

    List<Collect> selectAll(Collect collect);

    Collect selectById(Integer id);

    void insert(Collect collect);

    void updateById(Collect collect);

    void deleteById(Integer id);

    Collect selectByUserIdAndGoodsId(Collect collect);

}
