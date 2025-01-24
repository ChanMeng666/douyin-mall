package com.qxy.model.po;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class Cart {
    private Integer cartId;
    private Integer userId;
    private Date createdAt;
    private Date updatedAt;
    private List<CartItem> cartItems;
}