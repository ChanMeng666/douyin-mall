package com.qxy.model.po;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Author: water
 * Description: 订单实体类，用于映射数据库中的 orders 表
 * Date: 2025/1/24 21:26
 * Version: 1.0
 */

@Data
public class AiOrder {
    private Integer orderId;
    private Integer userId;
    private BigDecimal totalAmount;
    private String status;
    private Integer payType;
    private Timestamp payTime;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}