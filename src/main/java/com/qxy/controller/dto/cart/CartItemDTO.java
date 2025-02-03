package com.qxy.controller.dto.cart;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CartItemDTO {
    private Integer cartItemId;
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Date createAt;
    private Date updateAt;
}