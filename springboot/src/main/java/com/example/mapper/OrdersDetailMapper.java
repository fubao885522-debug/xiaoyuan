package com.example.mapper;

import com.example.entity.OrdersDetail;
import java.util.List;

/**
 * 订单明细数据接口
 */
public interface OrdersDetailMapper {

    int insert(OrdersDetail ordersDetail);

    int deleteById(Integer id);

    int deleteByOrderId(Integer orderId);

    List<OrdersDetail> selectByOrderId(Integer orderId);

}
