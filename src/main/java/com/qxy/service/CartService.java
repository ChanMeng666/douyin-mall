package com.qxy.service;

import com.qxy.controller.dto.cart.CartDTO;

public interface CartService {
    void createCart(Integer userId);
    CartDTO getCart(Integer userId);
    void addItem(Integer userId, Integer productId, Integer quantity);
    void removeItem(Integer cartItemId);
    void updateItemQuantity(Integer cartItemId, Integer quantity);
    void deleteCart(Integer cartId);
}