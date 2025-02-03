// com.qxy.service.AISimulationOrderService.java
package com.qxy.service;

import com.qxy.controller.dto.ai.AiOrderRequestDTO;
import com.qxy.controller.dto.ai.AiOrderResponseDTO;


/**
 * @author water
 * @description: AI模拟下单服务接口
 * @date 2025/2/3 20:15
 * @version 1.0
 */
public interface AISimulationOrderService {
    /**
     * 根据用户消息创建订单
     * @param requestDTO 用户请求参数（包含消息和用户ID）
     * @return 订单响应结果（支付链接、订单状态等）
     */
    AiOrderResponseDTO createOrderFromMessage(AiOrderRequestDTO requestDTO);
}