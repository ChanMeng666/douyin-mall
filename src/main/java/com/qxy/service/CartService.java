package com.qxy.service;

import com.qxy.model.po.Cart;

public interface CartService {
    void createCart(Integer userId);
    Cart getCart(Integer userId);
    void addItem(Integer userId, Integer productId, Integer quantity);
    void removeItem(Integer userId, Integer productId);
    void updateItemQuantity(Integer userId, Integer productId, Integer quantity);
    void clearCart(Integer userId);
}