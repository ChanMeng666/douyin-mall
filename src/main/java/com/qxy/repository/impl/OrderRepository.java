package com.qxy.repository.impl;

import com.qxy.model.dao.OrderDao;
import com.qxy.model.po.Order;
import com.qxy.repository.IOrderRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @Author: dawang
 * @Description: 订单表仓储实现类
 * @Date: 2025/1/20 23:03
 * @Version: 1.0
 */
@Repository
public class OrderRepository implements IOrderRepository {
    @Resource
    private OrderDao orderDao;
    @Override
    public void createOrder(Order order) {
        orderDao.createOrder(order);
    }
}
