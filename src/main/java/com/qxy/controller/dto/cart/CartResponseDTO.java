package com.qxy.controller.dto.cart;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponseDTO {
    private Integer cartId;
    private Integer userId;
    private List<CartItemDTO> items;
    private BigDecimal totalAmount;
}