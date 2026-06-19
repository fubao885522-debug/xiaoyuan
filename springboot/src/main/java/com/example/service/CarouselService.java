package com.example.service;

import com.example.entity.Carousel;
import com.example.mapper.CarouselMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 轮播图信息业务处理
 */
@Service
public class CarouselService {

    @Resource
    private CarouselMapper carouselMapper;

    public void add(Carousel carousel) {
        carouselMapper.insert(carousel);
    }

    public void deleteById(Integer id) {
        carouselMapper.deleteById(id);
    }

    public void updateById(Carousel carousel) {
        carouselMapper.updateById(carousel);
    }

    public Carousel selectById(Integer id) {
        return carouselMapper.selectById(id);
    }

    public List<Carousel> selectAll(Carousel carousel) {
        return carouselMapper.selectAll(carousel);
    }
}
