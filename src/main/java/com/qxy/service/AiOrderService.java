package com.qxy.service;

import com.qxy.model.res.AiOrderResponse;

/**
 * @Author: water
 * @Description: AI 订单服务接口，提供订单查询功能
 * @Date: 2025/1/22 16:44
 * @Version: 1.0
 */
public interface AiOrderService {
    AiOrderResponse getAiOrderById(String orderId);
}
