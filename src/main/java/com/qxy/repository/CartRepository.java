package com.qxy.repository;

import com.qxy.model.po.Cart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartRepository {
    void createCart(Cart cart);
    Cart getCartByUserId(Integer userId);
    void deleteCart(Integer cartId);
}