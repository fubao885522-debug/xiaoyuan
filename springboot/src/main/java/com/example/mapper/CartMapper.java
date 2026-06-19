package com.example.mapper;

import com.example.entity.Cart;
import java.util.List;

/**
 * 购物车数据访问
 */
public interface CartMapper {

    List<Cart> selectAll(Cart cart);

    Cart selectById(Integer id);

    void insert(Cart cart);

    void updateById(Cart cart);

    void deleteById(Integer id);

    void deleteByUserId(Integer userId);

    Cart selectByUserIdAndGoodsId(Cart cart);

}
