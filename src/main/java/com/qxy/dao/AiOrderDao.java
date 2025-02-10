package com.qxy.dao;

import com.qxy.model.po.AiOrder;
import com.qxy.model.po.OrderItems;
import com.qxy.model.po.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiOrderDao {
    List<AiOrder> findByUserId(@Param("userId") Integer userId);
    AiOrder findByOrderId(@Param("orderId") Integer orderId);
    void insertOrder(AiOrder order);
    void insertOrderItem(OrderItems orderItem); // 插入订单项
}