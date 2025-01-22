package com.qxy.service.impl;

import com.qxy.model.po.AiOrder;
import com.qxy.model.res.AiOrderResponse;
import com.qxy.service.AiOrderService;
import com.qxy.repository.AiOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: water
 * @Description: Ai订单服务实现类
 * @Date: 2025/1/22 16:44
 * @Version: 1.0
 */
@Service
public class AiOrderServiceImpl implements AiOrderService {

    @Autowired
    private AiOrderRepository aiOrderRepository;


    @Override
    public AiOrderResponse getAiOrderById(String orderId) {
        AiOrder order = aiOrderRepository.findByOrderId(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        // 构造响应对象
        AiOrderResponse response = new AiOrderResponse();
        response.setOrderId(order.getOrderId());
        response.setStatus(order.getStatus());
        return response;
    }
}
