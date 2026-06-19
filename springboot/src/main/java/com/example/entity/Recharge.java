package com.example.entity;

/**
 * 用户充值
 */
public class Recharge {

    /** ID */
    private Integer id;
    /** 充值金额 */
    private java.math.BigDecimal money;
    /** 充值人 */
    private Integer userId;
    /** 支付方式 */
    private String type;
    /** 充值时间 */
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public java.math.BigDecimal getMoney() {
        return money;
    }

    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
