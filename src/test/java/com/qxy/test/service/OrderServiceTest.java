package com.qxy.test.service;

import com.qxy.common.constant.Constants;
import com.qxy.controller.dto.order.CartItemDto;
import com.qxy.infrastructure.redis.IRedisService;
import com.qxy.model.po.CartItem;
import com.qxy.model.req.CreateOrderReq;
import com.qxy.model.res.CreateOrderRes;
import com.qxy.service.IOrderService;
import com.qxy.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RAtomicLong;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: dawang
 * @Description: 单元测试类
 * @Date: 2025/2/5 22:16
 * @Version: 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Resource
    private IOrderService orderService;

    @Resource
    private IRedisService redisService;

    @Resource
    private ProductService productService;


    // region 库存扣减测试
    @Test
    public void shouldSuccessWhenStockEnough() {
        // 初始化
        Integer productId = 1;

        mockRedisStock(productId, 100);
        List<CartItemDto> items = new ArrayList<>();
        CartItemDto item = CartItemDto.builder()
                .productId(productId)
                .quantity(2)
                .totalPrice(BigDecimal.valueOf(2 * 999.99))
                .build();
        items.add(item);
        CreateOrderReq request = buildValidRequest(1,items);
        // 执行
        CreateOrderRes response = orderService.createOrder(request);
        log.info("创建订单结果: {}", response);
    }

    // region 测试工具方法
    private void mockRedisStock(Integer productId, Integer stock) {
        String cacheKey = Constants.RedisKey.PRODUCT_COUNT_KEY + Constants.UNDERLINE + productId;
        redisService.setAtomicLong(cacheKey, stock);
    }
    private CreateOrderReq buildValidRequest(Integer userId, List<CartItemDto> items) {
        return CreateOrderReq.builder()
                .userId(userId)
                .cartId(1)
                .payType("wechat")
                .cartItems(convertCartItems(items))
                .build();
    }

    private List<CartItem> convertCartItems(List<CartItemDto> dtos) {
        List<CartItem> cartItems = dtos.stream().map(dto -> CartItem.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .totalPrice(dto.getTotalPrice())
                .build()).collect(Collectors.toList());
        return cartItems;
    }
}
