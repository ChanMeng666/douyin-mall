package com.qxy.repository.impl;

import com.qxy.model.po.AiOrder;
import com.qxy.repository.AiOrderRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: water
 * Description: 订单数据访问实现类，实现 IOrderRepository 接口
 * Date: 2025/1/24 21:26
 * Version: 1.0
 */

@Repository
public class AIOrderRepositoryImpl implements AiOrderRepository {


    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<AiOrder> findByUserId(Integer userId) {
        return sqlSession.selectList("com.qxy.repository.AiOrderRepository.findByUserId", userId);
    }

    @Override
    public AiOrder findByOrderId(Integer orderId) {
        return sqlSession.selectOne("com.qxy.repository.AiOrderRepository.findByOrderId", orderId);
    }
}