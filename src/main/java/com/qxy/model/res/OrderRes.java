package com.qxy.model.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;

/**
 * @Author: dawang
 * @Description: TODO
 * @Date: 2025/1/18 23:01
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRes {
    private Integer orderId;         // 订单ID
    private BigDecimal totalAmount; // 订单总金额
    private BigDecimal actualAmount; // 实际支付金额
    private String status;          // 订单状态（如 "pending", "paid"）
}
