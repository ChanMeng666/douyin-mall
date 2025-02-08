package com.qxy.controller.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindProductListDTO implements Serializable {
    //商品id
    private Integer productId;
    //商品名
    private String name;
    //商品描述
    private String description;
    //商品价格
    private BigDecimal price;
    //商品存货量
    private Integer stock;
    //商品图片
    private String imageUrl;
}
