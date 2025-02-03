package com.qxy.controller.dto.cart;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class CartDTO {
    private Integer cartId;
    private Integer userId;
    private Date createdAt;
    private Date updatedAt;
    private List<CartItemDTO> cartItems;
}
