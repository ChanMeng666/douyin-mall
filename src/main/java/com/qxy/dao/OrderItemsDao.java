package com.qxy.dao;

import com.qxy.model.po.OrderItems;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: dawang
 * @Description: TODO
 * @Date: 2025/1/20 23:15
 * @Version: 1.0
 */
@Mapper
public interface OrderItemsDao {

    void insertOrderItems(OrderItems orderItems);
}
