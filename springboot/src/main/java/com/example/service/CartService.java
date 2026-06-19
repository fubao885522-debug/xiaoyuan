package com.example.service;

import com.example.entity.Cart;
import com.example.entity.Goods;
import com.example.mapper.CartMapper;
import com.example.mapper.GoodsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车业务处理
 */
@Service
public class CartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private GoodsMapper goodsMapper;

    /**
     * 加入购物车：如果该商品已在购物车中，则增加数量；否则新增
     */
    public void add(Cart cart) {
        Cart exist = cartMapper.selectByUserIdAndGoodsId(cart);
        if (exist != null) {
            // 已存在，数量累加
            exist.setNum(exist.getNum() + cart.getNum());
            cartMapper.updateById(exist);
        } else {
            cartMapper.insert(cart);
        }
    }

    public void deleteById(Integer id) {
        cartMapper.deleteById(id);
    }

    /**
     * 清空用户购物车
     */
    public void deleteByUserId(Integer userId) {
        cartMapper.deleteByUserId(userId);
    }

    public void updateById(Cart cart) {
        cartMapper.updateById(cart);
    }

    public Cart selectById(Integer id) {
        return cartMapper.selectById(id);
    }

    /**
     * 查询购物车列表，自动过滤下架商品
     */
    public List<Cart> selectAll(Cart cart) {
        List<Cart> list = cartMapper.selectAll(cart);
        return filterOffShelf(list);
    }

    public PageInfo<Cart> selectPage(Cart cart, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Cart> list = selectAll(cart);
        return PageInfo.of(list);
    }

    /**
     * 过滤下架商品：检查商品状态，下架的商品自动从购物车移除
     */
    private List<Cart> filterOffShelf(List<Cart> cartList) {
        List<Cart> result = new ArrayList<>();
        for (Cart cart : cartList) {
            Goods goods = goodsMapper.selectById(cart.getGoodsId());
            if (goods != null && !"下架".equals(goods.getStatus())) {
                result.add(cart);
            } else if (goods == null || "下架".equals(goods.getStatus())) {
                // 商品不存在或已下架，从购物车中删除
                cartMapper.deleteById(cart.getId());
            }
        }
        return result;
    }

}
