package com.qxy.repository;

import com.qxy.model.po.Order;

/**
 * @Author: dawang
 * @Description: 订单表仓储接口
 * @Date: 2025/1/20 23:03
 * @Version: 1.0
 */
public interface IOrderRepository {
    void createOrder(Order order);

    /**
     * 根据用户ID获取订单列表
     * @param userId
     * @return
     */
    Order getOrderList(Integer userId);
}
