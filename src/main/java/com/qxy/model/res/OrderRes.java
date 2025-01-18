package com.qxy.model.res;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: dawang
 * @Description: TODO
 * @Date: 2025/1/18 23:01
 * @Version: 1.0
 */
@Data
public class OrderRes {
    private String orderId;         // 订单ID
    private BigDecimal totalAmount; // 订单总金额
    private BigDecimal actualAmount; // 实际支付金额
    private String status;          // 订单状态（如 "pending", "paid"）
}
