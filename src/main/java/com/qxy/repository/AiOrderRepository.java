package com.qxy.repository;

import com.qxy.model.po.AiOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Author: water
 * Description: 订单数据访问接口，定义与 orders 表交互的方法
 * Date: 2025/1/24 21:26
 * Version: 1.0
 */

@Mapper
public interface AiOrderRepository {
    List<AiOrder> findByUserId(@Param("userId") Integer userId);
    AiOrder findByOrderId(@Param("orderId") Integer orderId);
}
