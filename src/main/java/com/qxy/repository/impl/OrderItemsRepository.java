package com.qxy.repository.impl;

import com.qxy.model.dao.OrderItemsDao;
import com.qxy.model.po.CartItem;
import com.qxy.model.po.OrderItems;
import com.qxy.repository.IOrderItemsRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: dawang
 * @Description: 订单商品仓储实现类
 * @Date: 2025/1/20 23:07
 * @Version: 1.0
 */
public class OrderItemsRepository implements IOrderItemsRepository {
    private OrderItemsDao orderItemsDao;
    @Override
    public void insertOrderItems(Integer orderId, List<CartItem> cartItems) {
        List<OrderItems> orderItemsList = new ArrayList<>(cartItems.size());
        for (CartItem cartItem : cartItems) {
            OrderItems orderItem = new OrderItems();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItemsList.add(orderItem);
        }
        for(OrderItems orderItem : orderItemsList) {
            orderItemsDao.insertOrderItems(orderItem);
        }

    }
}
