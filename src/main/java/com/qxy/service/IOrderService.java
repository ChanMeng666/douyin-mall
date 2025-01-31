package com.qxy.service;

import com.qxy.model.po.CartItem;
import com.qxy.model.po.Order;
import com.qxy.model.req.CreateOrderReq;
import com.qxy.model.res.OrderRes;

import java.util.List;

/**
 * @Author: dawang
 * @Description: 订单服务接口
 * @Date: 2025/1/18 22:19
 * @Version: 1.0
 */

public interface IOrderService {

    /**
     * 创建订单
     * @param createOrderReq
     * @return
     */
    OrderRes createOrder(CreateOrderReq createOrderReq);

    /**
     * 获取订单列表
     * @param userId
     * @return
     */
    Order getOrderList(Integer userId);

    /**
     * 获取超时订单
     * @return
     */
    List<Integer> getOvertimeOrders();

    /**
     * 更新订单状态为取消
     * @param orderId
     */
    void updateOrderStatusToCancelled(int orderId);

    /**
     * 从延迟队列中取一个购物车
     * @return
     */
    CartItem takeQueue();
}
