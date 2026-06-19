package com.example.mapper;

import com.example.entity.Carousel;

import java.util.List;

/**
 * 操作carousel相关数据接口
 */
public interface CarouselMapper {

    int insert(Carousel carousel);
    int deleteById(Integer id);
    int updateById(Carousel carousel);
    Carousel selectById(Integer id);
    List<Carousel> selectAll(Carousel carousel);

}
