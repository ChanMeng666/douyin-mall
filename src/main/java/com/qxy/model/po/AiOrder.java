package com.qxy.model.po;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: water
 * @Description: 订单实体类，映射数据库中的 "order" 表
 * @Date: 2025/1/22 16:44
 * @Version: 1.0
 */
@Entity
@Table(name = "order")
public class AiOrder {
    @Id
    private String orderId;
    private String status;

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
