package com.qxy.model.dao;

import com.qxy.model.po.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: dawang
 * @Description: 订单数据库orm
 * @Date: 2025/1/18 22:15
 * @Version: 1.0
 */

@Mapper
public interface OrderDao {
   void createOrder(Order order);
}
