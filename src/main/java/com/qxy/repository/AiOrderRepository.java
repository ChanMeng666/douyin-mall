package com.qxy.repository;

import com.qxy.model.po.AiOrder;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: water
 * @Description: 订单数据访问接口
 * @Date: 2025/1/22 16:44
 * @Version: 1.0
 */
public interface AiOrderRepository extends JpaRepository<AiOrder, String> {
    AiOrder findByOrderId(String orderId);
}