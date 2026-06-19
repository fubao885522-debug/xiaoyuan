package com.example.mapper;

import com.example.entity.Orders;
import java.util.List;

/**
 * 订单数据接口
 */
public interface OrdersMapper {

    int insert(Orders orders);

    int deleteById(Integer id);

    int updateById(Orders orders);

    Orders selectById(Integer id);

    List<Orders> selectAll(Orders orders);

}
