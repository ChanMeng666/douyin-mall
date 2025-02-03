// com.qxy.service.dto.AiOrderResponseDTO.java
package com.qxy.controller.dto.ai;

import lombok.Data;

/**
 * @author water
 * @description: AI下单响应DTO
 * @date 2025/2/3 20:15
 * @version 1.0
 */
@Data
public class AiOrderResponseDTO {
    private String paymentUrl;
    private String orderId;
    private String status;
}