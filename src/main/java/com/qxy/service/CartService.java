package com.qxy.service;

import com.qxy.model.po.Cart;

public interface CartService {
    void createCart(Integer userId);
    Cart getCart(Integer userId);
    void addItem(Integer userId, Integer productId, Integer quantity);
    void removeItem(Integer cartItemId);
    void updateItemQuantity(Integer cartItemId, Integer quantity);
    void deleteCart(Integer cartId);
}