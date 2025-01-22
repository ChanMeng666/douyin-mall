package com.qxy.model.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: dawang
 * @Description: 购物车商品表
 * @Date: 2025/1/18 22:47
 * @Version: 1.0
 */
@Data
public class CartItem {
    /** 购物车项ID */
    private Integer cartItemId;
    /** 购物车ID */
    private Integer cartId;
    /** 商品ID */
    private Integer productId;
    /** 商品数量 */
    private Integer quantity;
    /** 商品价格 */
    private BigDecimal price;
    /** 创建时间 */
    private Date createAt;
    /** 更新时间 */
    private Date updateAt;
}