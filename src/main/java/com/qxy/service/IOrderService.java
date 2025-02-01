package com.qxy.service;

import com.qxy.model.po.CartItem;
import com.qxy.model.po.Order;
import com.qxy.model.req.CreateOrderReq;
import com.qxy.model.req.QueryHistoryOrderReq;
import com.qxy.model.res.CreateOrderRes;
import com.qxy.model.res.QueryHistoryOrderRes;

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
    CreateOrderRes createOrder(CreateOrderReq createOrderReq);

    /**
     * 根据用户id查询订单
     * @param queryHistoryOrderReq
     * @return
     */
    QueryHistoryOrderRes queryHistoryOrderByUserId( QueryHistoryOrderReq queryHistoryOrderReq);

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
     * 从延迟队列中取一个购物车,用于消费商品库存
     * @return
     */
    CartItem takeQueue();
}
