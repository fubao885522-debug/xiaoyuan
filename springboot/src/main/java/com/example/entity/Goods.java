package com.example.entity;

import java.math.BigDecimal;

/**
 * 商品信息
 */
public class Goods {

    /** ID */
    private Integer id;
    /** 名称 */
    private String name;
    /** 图片 */
    private String img;
    /** 价格 */
    private BigDecimal price;
    /** 简介 */
    private String description;
    /** 详情 */
    private String content;
    /** 库存 */
    private Integer store;
    /** 分类ID */
    private Integer categoryId;
    /** 上架状态 */
    private String status;
    /** 是否推荐 */
    private Boolean recommend;
    /** 浏览量 */
    private Integer views;
    /** 销量 */
    private Integer saleCount;
    /** 创建时间 */
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
