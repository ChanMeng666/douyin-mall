package com.qxy.repository;

import com.qxy.model.po.CartItem;

import java.util.List;

/**
 * @Author: dawang
 * @Description: 订单商品表仓储接口
 * @Date: 2025/1/20 23:05
 * @Version: 1.0
 */
public interface IOrderItemsRepository {
    void insertOrderItems(Integer orderId, List<CartItem> cartItems);
}
