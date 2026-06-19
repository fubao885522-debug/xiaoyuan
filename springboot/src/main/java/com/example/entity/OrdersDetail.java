package com.example.entity;

import java.math.BigDecimal;

/**
 * 订单明细
 */
public class OrdersDetail {

    /** ID */
    private Integer id;
    /** 订单ID */
    private Integer orderId;
    /** 商品ID */
    private Integer goodsId;
    /** 商品名称 */
    private String goodsName;
    /** 商品图片 */
    private String goodsImg;
    /** 商品单价 */
    private BigDecimal price;
    /** 数量 */
    private Integer num;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

}
