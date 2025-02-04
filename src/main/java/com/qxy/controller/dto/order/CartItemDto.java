package com.qxy.controller.dto.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: dawang
 * @Description: 购物车dto
 * @Date: 2025/2/3 22:10
 * @Version: 1.0
 */
@Data
public class CartItemDto {
    /** 购物车项ID */
    private Integer cartItemId;
    /** 购物车ID */
    private Integer cartId;
    /** 商品ID */
    private Integer productId;
    /** 商品数量 */
    private Integer quantity;
    /** 商品价格 */
//    private BigDecimal price;
    private BigDecimal totalPrice; // Changed to match database schema type
}
