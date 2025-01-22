package com.qxy.controller;

import com.qxy.model.res.AiOrderResponse;
import com.qxy.service.AiOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: water
 * @Description: AI 订单控制器
 * @Date: 2025/1/22 16:44
 * @Version: 1.0
 */
@RestController
public class AiOrderController {

    @Autowired
    private AiOrderService aiOrderService;

    @GetMapping("/api/ai/order/query")
    public AiOrderResponse queryOrder(@RequestParam String orderId) {
        return aiOrderService.getAiOrderById(orderId);
    }
}