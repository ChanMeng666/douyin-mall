package com.qxy.test.service;

import com.qxy.common.constant.Constants;
import com.qxy.controller.dto.order.CartItemDto;
import com.qxy.dao.dataobject.ProductDO;
import com.qxy.dao.mapper.ProductDao;
import com.qxy.infrastructure.redis.IRedisService;
import com.qxy.model.po.Product;
import com.qxy.model.req.CreateOrderReq;
import com.qxy.model.res.CreateOrderRes;
import com.qxy.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RAtomicLong;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: dawang
 * @Description: 订单测试类
 * @Date: 2025/2/4 21:16
 * @Version: 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {
    @Resource
    private IRedisService redisService;

    @Resource
    private IOrderService orderService;

    @Resource
    private ProductDao productDao;
    /**\
     * 初始化redis库存
     * */
    @Test
    public void initRedisStock(){
        List<Product> products= productDao.queryProducts();
        for(Product product : products) {
            String cacheKey = Constants.RedisKey.PRODUCT_COUNT_KEY + Constants.UNDERLINE + product.getProductId();
            redisService.setValue(cacheKey, product.getStock());
        }
    }

}
