package com.qxy.service.impl;

import com.qxy.dao.AiOrderDao;
import com.qxy.model.po.AiOrder;
import com.qxy.service.AiOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: water
 * Description: 订单服务实现类，实现 AIOrderService 接口
 * Date: 2025/1/24 21:26
 * Version: 1.0
 */
@Service
public class AIOrderServiceImpl implements AiOrderService {
    @Autowired
    private AiOrderDao aiOrderDao;

    @Override
    public List<AiOrder> getOrdersByUserId(Integer userId) {
        return aiOrderDao.findByUserId(userId);
    }

    @Override
    public AiOrder getOrderByOrderId(Integer orderId) {
        return aiOrderDao.findByOrderId(orderId);
    }
}
