package com.qxy.controller.param;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductParam implements Serializable {
    //商品名
    private String name;
    //商品描述
    private String description;
    //商品价格
    private BigDecimal price;
    //存货量
    private Integer stock;
}
