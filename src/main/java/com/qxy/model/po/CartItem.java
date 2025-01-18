package com.qxy.model.po;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: dawang
 * @Description: TODO
 * @Date: 2025/1/18 22:47
 * @Version: 1.0
 */
@Data
public class CartItem {

    private Long productId;      // 产品ID
    private int quantity;        // 数量
    private BigDecimal price;    // 商品单价

    // getters and setters
}