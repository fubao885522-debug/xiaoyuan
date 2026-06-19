package com.example.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 数据统计
 */
public interface DashboardMapper {

    /** 统计各类数量 */
    @Select("select " +
            "(select count(*) from `goods`) as goodsCount, " +
            "(select count(*) from `orders`) as ordersCount, " +
            "(select count(*) from `comment`) as commentCount, " +
            "(select count(*) from `user`) as userCount")
    Map<String, Object> countAll();

    /** 今日订单数 */
    @Select("select count(*) from `orders` where date(`time`) = curdate()")
    Integer countTodayOrders();

    /** 今日销售额 */
    @Select("select coalesce(sum(total_price), 0) from `orders` where date(`time`) = curdate() and order_status != '已取消'")
    java.math.BigDecimal sumTodaySales();

    /** 近7天每日销售额 */
    @Select("select date(`time`) as date, coalesce(sum(total_price), 0) as amount " +
            "from `orders` where date(`time`) >= date_sub(curdate(), interval 7 day) and order_status != '已取消' " +
            "group by date(`time`) order by date(`time`)")
    List<Map<String, Object>> salesByDay7();

    /** 商品销量 Top5 */
    @Select("select g.name, coalesce(sum(od.num), 0) as num " +
            "from orders_detail od left join goods g on od.goods_id = g.id " +
            "group by od.goods_id, g.name order by num desc limit 5")
    List<Map<String, Object>> top5Goods();

    /** 各订单状态数量 */
    @Select("select order_status as name, count(*) as value from `orders` group by order_status")
    List<Map<String, Object>> orderStatusCount();

    /** 总销售额（已完成+已出货+已发货） */
    @Select("select coalesce(sum(total_price), 0) from `orders` where order_status in ('已完成', '已出货', '已发货')")
    java.math.BigDecimal sumTotalSales();

    /** 注册用户数 */
    @Select("select count(*) from `user`")
    Integer countUsers();

    /** 按商品分类统计销售额 */
    @Select("select c.name, coalesce(sum(od.num * od.price), 0) as amount " +
            "from orders_detail od " +
            "left join goods g on od.goods_id = g.id " +
            "left join category c on g.category_id = c.id " +
            "group by c.id, c.name order by amount desc")
    List<Map<String, Object>> salesByCategory();
}
