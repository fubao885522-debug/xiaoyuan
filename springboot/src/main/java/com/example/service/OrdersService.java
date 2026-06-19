package com.example.service;

import com.example.entity.Cart;
import com.example.entity.Goods;
import com.example.entity.Orders;
import com.example.entity.OrdersDetail;
import com.example.entity.User;
import com.example.mapper.CartMapper;
import com.example.mapper.GoodsMapper;
import com.example.mapper.OrdersDetailMapper;
import com.example.mapper.OrdersMapper;
import com.example.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单业务处理
 */
@Service
public class OrdersService {

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrdersDetailMapper ordersDetailMapper;

    @Resource
    private CartMapper cartMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 创建订单（从购物车结算）
     * 1. 生成订单编号
     * 2. 插入订单主表
     * 3. 插入订单明细
     * 4. 清空购物车
     */
    @Transactional
    public Orders createFromCart(Integer userId, String userName) {
        // 查询用户购物车
        Cart query = new Cart();
        query.setUserId(userId);
        List<Cart> cartList = cartMapper.selectAll(query);

        if (cartList == null || cartList.isEmpty()) {
            throw new RuntimeException("购物车为空");
        }

        // 计算总金额
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Cart cart : cartList) {
            BigDecimal itemTotal = cart.getPrice().multiply(new BigDecimal(cart.getNum()));
            totalPrice = totalPrice.add(itemTotal);
        }

        // 创建订单
        Orders orders = new Orders();
        orders.setOrderNo("ORD" + System.currentTimeMillis());
        orders.setUserId(userId);
        orders.setUserName(userName);
        orders.setTotalPrice(totalPrice);
        orders.setStatus("已支付");
        orders.setDeliveryType("");
        orders.setOrderStatus("待发货");
        orders.setAddress("");
        orders.setDeliver("");
        orders.setTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        ordersMapper.insert(orders);

        // 插入订单明细
        for (Cart cart : cartList) {
            OrdersDetail detail = new OrdersDetail();
            detail.setOrderId(orders.getId());
            detail.setGoodsId(cart.getGoodsId());
            detail.setGoodsName(cart.getGoodsName());
            detail.setGoodsImg(cart.getGoodsImg());
            detail.setPrice(cart.getPrice());
            detail.setNum(cart.getNum());
            ordersDetailMapper.insert(detail);
        }

        // 清空购物车
        cartMapper.deleteByUserId(userId);

        return orders;
    }

    /**
     * 立即购买（单个商品直接下单）
     */
    @Transactional
    public Orders buyNow(Integer goodsId, String goodsName, String goodsImg, BigDecimal price, Integer num,
                         Integer userId, String userName, String deliveryType, String address, String deliver) {
        Orders orders = new Orders();
        orders.setOrderNo("ORD" + System.currentTimeMillis());
        orders.setUserId(userId);
        orders.setUserName(userName);
        orders.setTotalPrice(price.multiply(new BigDecimal(num)));
        orders.setStatus("已支付");
        orders.setDeliveryType(deliveryType != null ? deliveryType : "");
        orders.setOrderStatus("待发货");
        orders.setAddress(address != null ? address : "");
        orders.setDeliver(deliver != null ? deliver : "");
        orders.setTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        ordersMapper.insert(orders);

        OrdersDetail detail = new OrdersDetail();
        detail.setOrderId(orders.getId());
        detail.setGoodsId(goodsId);
        detail.setGoodsName(goodsName);
        detail.setGoodsImg(goodsImg);
        detail.setPrice(price);
        detail.setNum(num);
        ordersDetailMapper.insert(detail);

        // 扣减库存 + 增加销量
        Goods dbGoods = goodsMapper.selectById(goodsId);
        if (dbGoods != null) {
            dbGoods.setStore(Math.max(0, (dbGoods.getStore() != null ? dbGoods.getStore() : 0) - num));
            dbGoods.setSaleCount((dbGoods.getSaleCount() != null ? dbGoods.getSaleCount() : 0) + num);
            goodsMapper.updateById(dbGoods);
        }

        return orders;
    }

    public void deleteById(Integer id) {
        ordersDetailMapper.deleteByOrderId(id);
        ordersMapper.deleteById(id);
    }

    public void updateById(Orders orders) {
        // 先从数据库查出完整订单，再合并更新字段，避免null覆盖
        Orders dbOrders = ordersMapper.selectById(orders.getId());
        if (dbOrders != null) {
            if (orders.getOrderNo() != null) dbOrders.setOrderNo(orders.getOrderNo());
            if (orders.getUserId() != null) dbOrders.setUserId(orders.getUserId());
            if (orders.getUserName() != null) dbOrders.setUserName(orders.getUserName());
            if (orders.getTotalPrice() != null) dbOrders.setTotalPrice(orders.getTotalPrice());
            if (orders.getStatus() != null) dbOrders.setStatus(orders.getStatus());
            if (orders.getDeliveryType() != null) dbOrders.setDeliveryType(orders.getDeliveryType());
            if (orders.getOrderStatus() != null) dbOrders.setOrderStatus(orders.getOrderStatus());
            if (orders.getAddress() != null) dbOrders.setAddress(orders.getAddress());
            if (orders.getDeliver() != null) dbOrders.setDeliver(orders.getDeliver());
            if (orders.getTime() != null) dbOrders.setTime(orders.getTime());
            ordersMapper.updateById(dbOrders);
        }
    }

    /**
     * 取消订单
     * 1. 校验订单状态（只有"待发货"才能取消）
     * 2. 退款：将订单金额退回用户余额
     * 3. 更新订单状态为"已取消"
     */
    @Transactional
    public void cancelOrder(Integer id) {
        Orders orders = ordersMapper.selectById(id);
        if (orders == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!"待发货".equals(orders.getOrderStatus()) && !"已出货".equals(orders.getOrderStatus())) {
            throw new RuntimeException("只有未出货或已出货状态的订单才能取消");
        }

        // 退款：将订单金额退回用户余额
        if (orders.getUserId() != null && orders.getTotalPrice() != null) {
            User user = userMapper.selectById(orders.getUserId());
            if (user != null) {
                BigDecimal current = user.getAccount() != null ? user.getAccount() : BigDecimal.ZERO;
                user.setAccount(current.add(orders.getTotalPrice()));
                userMapper.updateById(user);
            }
        }

        // 恢复库存 + 回退销量
        List<OrdersDetail> details = ordersDetailMapper.selectByOrderId(id);
        if (details != null) {
            for (OrdersDetail detail : details) {
                Goods goods = goodsMapper.selectById(detail.getGoodsId());
                if (goods != null) {
                    goods.setStore((goods.getStore() != null ? goods.getStore() : 0) + detail.getNum());
                    goods.setSaleCount(Math.max(0, (goods.getSaleCount() != null ? goods.getSaleCount() : 0) - detail.getNum()));
                    goodsMapper.updateById(goods);
                }
            }
        }

        // 更新订单状态
        orders.setStatus("已取消");
        orders.setOrderStatus("已取消");
        ordersMapper.updateById(orders);
    }

    public Orders selectById(Integer id) {
        Orders orders = ordersMapper.selectById(id);
        if (orders != null) {
            List<OrdersDetail> details = ordersDetailMapper.selectByOrderId(id);
            orders.setDetails(details);
        }
        return orders;
    }

    public List<Orders> selectAll(Orders orders) {
        List<Orders> list = ordersMapper.selectAll(orders);
        // 为每个订单加载明细
        for (Orders o : list) {
            List<OrdersDetail> details = ordersDetailMapper.selectByOrderId(o.getId());
            o.setDetails(details);
        }
        return list;
    }

    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = selectAll(orders);
        return PageInfo.of(list);
    }

}
