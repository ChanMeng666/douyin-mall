package com.qxy.service.impl;

import com.qxy.model.po.AiOrder;
import com.qxy.service.AiOrderService;
import com.qxy.service.QwenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: water
 * Description: AI 服务类，结合通义千问 AI 模型和订单查询逻辑
 * Date: 2025/1/24 21:26
 * Version: 1.0
 */
@Service
public class AIServiceImpl {

    @Qualifier("AIOrderServiceImpl")
    @Autowired
    private AiOrderService orderService;

    @Autowired
    private QwenAIService qwenAIService;

    public String handleUserInput(String userInput) {
        try {
            // 调用通义千问 AI 模型解析用户输入
            String aiResponse = qwenAIService.callQwenAI(userInput);

            // 解析 AI 返回的内容，提取用户 ID 或订单 ID
            Integer userId = extractUserId(aiResponse);
            Integer orderId = extractOrderId(aiResponse);

            if (userId != null) {
                // 查询用户订单
                List<AiOrder> orders = orderService.getOrdersByUserId(userId);
                return formatOrders(orders);
            } else if (orderId != null) {
                // 查询特定订单
                AiOrder order = orderService.getOrderByOrderId(orderId);
                return formatOrder(order);
            } else {
                return "未找到相关订单信息。";
            }
        } catch (Exception e) {
            return "处理请求时发生错误：" + e.getMessage();
        }
    }

    private Integer extractUserId(String aiResponse) {
        // 解析 AI 返回的内容，提取用户 ID
        try {
            return Integer.parseInt(aiResponse.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer extractOrderId(String aiResponse) {
        // 解析 AI 返回的内容，提取订单 ID
        try {
            return Integer.parseInt(aiResponse.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String formatOrders(List<AiOrder> orders) {
        if (orders.isEmpty()) {
            return "没有找到该用户的订单。";
        }

        StringBuilder sb = new StringBuilder();
        for (AiOrder order : orders) {
            sb.append("订单ID: ").append(order.getOrderId())
                    .append(", 用户ID: ").append(order.getUserId())
                    .append(", 总金额: ").append(order.getTotalAmount())
                    .append(", 状态: ").append(order.getStatus())
                    .append("\n");
        }
        return sb.toString();
    }

    private String formatOrder(AiOrder order) {
        if (order == null) {
            return "未找到该订单。";
        }
        return "订单ID: " + order.getOrderId() +
                ", 用户ID: " + order.getUserId() +
                ", 总金额: " + order.getTotalAmount() +
                ", 状态: " + order.getStatus();
    }
}