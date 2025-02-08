package com.qxy.test.dao;

import com.qxy.dao.OrderDao;
import com.qxy.model.po.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author: dawang
 * @Description: orderDao测试类
 * @Date: 2025/2/8 21:28
 * @Version: 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDaoTest {
    @Resource
    private OrderDao orderDao;


    @Test
    public void insertOrderTest(){
        Order order = new Order();
        order.setUserId(1);
        order.setStatus("待支付");
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setPayType("wechat");
        orderDao.createOrder(order);
        log.info("订单创建成功: orderId:{} , totalAmount:{}", order.getOrderId(), order.getTotalAmount());
    }
}
