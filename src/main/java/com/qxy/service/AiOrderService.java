package com.qxy.service;

import com.qxy.model.po.AiOrder;

import java.util.List;

/**
 * Author: water
 * Description: 订单服务接口，定义订单相关的业务逻辑方法
 * Date: 2025/1/24 21:26
 * Version: 1.0
 */
public interface AiOrderService {
    List<AiOrder> getOrdersByUserId(Integer userId);
    AiOrder getOrderByOrderId(Integer orderId);
}
