package com.example.entity;

/**
 * 商品评论
 */
public class Comment {

    /** ID */
    private Integer id;
    /** 商品ID */
    private Integer goodsId;
    /** 用户ID */
    private Integer userId;
    /** 用户名 */
    private String userName;
    /** 评论内容 */
    private String content;
    /** 评分 1-5 */
    private Integer rating;
    /** 订单ID */
    private Integer orderId;
    /** 订单编号（关联查询，非数据库字段） */
    private String orderNo;
    /** 商品名称（关联查询，非数据库字段） */
    private String goodsName;
    /** 商品图片（关联查询，非数据库字段） */
    private String goodsImg;
    /** 创建时间 */
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
