package com.qxy.model.po;

import com.qxy.model.enums.ProductStatus;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author water
 * @description: 商品实体类
 * @date 2025/2/3 20:15
 * @version 1.0
 */
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @NotBlank(message = "商品名称不能为空")
    private String name;

    private String description;

    @NotNull(message = "商品价格不能为空")
    @PositiveOrZero(message = "商品价格必须大于或等于0")
    private BigDecimal price;

    @NotNull(message = "商品库存不能为空")
    @PositiveOrZero(message = "商品库存必须大于或等于0")
    private Integer stock;

    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

    private String imageUrl;

    private ProductStatus status = ProductStatus.ACTIVE;
}