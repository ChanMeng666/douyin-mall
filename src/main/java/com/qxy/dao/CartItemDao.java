package com.qxy.dao;

import com.qxy.model.po.CartItem;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CartItemDao {
    void insertItem(CartItem cartItem);
    void deleteItem(Integer cartItemId);
    void updateItem(CartItem cartItem);
    List<CartItem> getItemsByCartId(Integer cartId);
    CartItem getItemByProductId(Integer cartId, Integer productId);
    void updateTotalPrice(Integer cartItemId, Double totalPrice);
}