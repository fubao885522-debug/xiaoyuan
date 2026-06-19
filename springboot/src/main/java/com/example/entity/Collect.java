package com.example.entity;

/**
 * 用户收藏
 */
public class Collect {

    /** ID */
    private Integer id;
    /** 用户ID */
    private Integer userId;
    /** 商品ID */
    private Integer goodsId;
    /** 商品名称 */
    private String goodsName;
    /** 商品图片 */
    private String goodsImg;
    /** 商品价格 */
    private java.math.BigDecimal goodsPrice;
    /** 收藏时间 */
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public java.math.BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(java.math.BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
