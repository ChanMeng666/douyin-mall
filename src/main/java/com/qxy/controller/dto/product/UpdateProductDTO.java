package com.qxy.controller.dto.product;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class UpdateProductDTO implements Serializable {
    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 商品名
     */
    private String name;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 商品价格
     */
    private BigDecimal price;
    /**
     * 存货量
     */
    private Integer stock;
}
