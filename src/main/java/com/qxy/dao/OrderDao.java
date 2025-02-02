package com.qxy.dao;

import com.qxy.model.po.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: dawang
 * @Description: 订单数据库orm
 * @Date: 2025/1/18 22:15
 * @Version: 1.0
 */

@Mapper
public interface OrderDao {
   List<Order> getOrderList(Integer userId);

   void createOrder(Order order);

    List<Integer> getOvertimeOrders();

    void updateOrderStatusToCancelled(int orderId);
}
