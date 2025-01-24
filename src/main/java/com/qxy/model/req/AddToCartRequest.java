package com.qxy.model.req;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Integer userId;
    private Integer productId;
    private Integer quantity;
}