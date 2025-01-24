package com.qxy.service;

import com.qxy.model.po.Order;
import com.qxy.model.req.CreateOrderReq;
import com.qxy.model.res.OrderRes;

/**
 * @Author: dawang
 * @Description: 订单服务接口
 * @Date: 2025/1/18 22:19
 * @Version: 1.0
 */

public interface IOrderService {

    OrderRes createOrder(CreateOrderReq createOrderReq);

    Order getOrderList(Integer userId);
}
