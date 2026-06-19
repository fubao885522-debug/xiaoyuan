package com.example.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单
 */
public class Orders {

    /** ID */
    private Integer id;
    /** 订单编号 */
    private String orderNo;
    /** 用户ID */
    private Integer userId;
    /** 用户名 */
    private String userName;
    /** 总金额 */
    private BigDecimal totalPrice;
    /** 状态 */
    private String status;
    /** 配送类型 */
    private String deliveryType;
    /** 订单状态 */
    private String orderStatus;
    /** 收货地址 */
    private String address;
    /** 配送信息 */
    private String deliver;
    /** 时间 */
    private String time;

    /** 查询用：开始时间（非数据库字段） */
    private String startTime;
    /** 查询用：结束时间（非数据库字段） */
    private String endTime;

    /** 订单明细列表（非数据库字段，用于前端展示） */
    private List<OrdersDetail> details;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<OrdersDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrdersDetail> details) {
        this.details = details;
    }

}
