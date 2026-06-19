package com.example.entity;

/**
 * 轮播图信息
 */
public class Carousel {

    /** ID */
    private Integer id;
    /** 商品ID */
    private Integer goodsId;
    /** 图片 */
    private String img;
    /** 商品名称 */
    private String goodsName;

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

}
