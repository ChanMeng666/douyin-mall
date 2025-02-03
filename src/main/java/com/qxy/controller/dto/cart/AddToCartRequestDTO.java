package com.qxy.controller.dto.cart;

import lombok.Data;

@Data
public class AddToCartRequestDTO {
    private Integer userId;
    private Integer productId;
    private Integer quantity;
}