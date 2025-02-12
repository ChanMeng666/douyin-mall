package com.qxy.service.impl;

import com.qxy.common.constant.Constants;
import com.qxy.common.exception.AppException;
import com.qxy.common.exception.AppExceptionCode;
import com.qxy.dao.CartItemDao;
import com.qxy.dao.OrderDao;
import com.qxy.dao.OrderItemsDao;
import com.qxy.infrastructure.redis.RedissonService;
import com.qxy.model.po.CartItem;
import com.qxy.model.po.Order;
import com.qxy.model.po.OrderItems;
import com.qxy.model.req.CreateOrderReq;
import com.qxy.model.req.QueryHistoryOrderReq;
import com.qxy.model.res.CreateOrderRes;
import com.qxy.model.res.QueryHistoryOrderRes;
import com.qxy.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @Author: dawang
 * @Description: 订单服务实现类
 * @Date: 2025/1/18 22:14
 * @Version: 1.0
 */
@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemsDao orderItemsDao;

    @Resource
    private CartItemDao cartItemDao;


    @Resource
    private RedissonService redissonService;

    // 新增库存回滚记录器
    private static final ThreadLocal<Map<Integer, Integer>> stockRollbackHolder =
            ThreadLocal.withInitial(HashMap::new);

    @Override
    public CreateOrderRes createOrder(CreateOrderReq createOrderReq) {
        log.info("创建订单请求: userId:{} , cartId:{} , payType:{}", createOrderReq.getUserId(),createOrderReq.getCartId(), createOrderReq.getPayType());
        Order order = new Order();

        // 获取购物车商品信息
        List<CartItem> cartItems = createOrderReq.getCartItems();

        //减少对应商品库存
        boolean stockStatus = subtractProductStock(cartItems);

        //库存不足
        if (!stockStatus) {
            return null;
        }
        // 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            totalAmount = totalAmount.add(cartItem.getTotalPrice().multiply(new BigDecimal(cartItem.getQuantity())));
        }
        order.setTotalAmount(totalAmount);

        //设置订单的支付类型
        order.setPayType(createOrderReq.getPayType());

        //设置订单状态
        order.setStatus(Constants.OrderStatus.PAY_WAIT.getCode());

        //设置用户id
        order.setUserId(createOrderReq.getUserId());

        //保存订单
        orderDao.createOrder(order);
        log.info("订单创建成功: orderId:{} , totalAmount:{}", order.getOrderId(), order.getTotalAmount());
        //保存订单商品项
        insertOrderItems(order.getOrderId(), cartItems);

        //清除购物车商品
        for(CartItem cartItem : cartItems) {
            cartItemDao.deleteItem(cartItem.getCartItemId());
        }



        //返回订单响应
        return CreateOrderRes.builder()
                .status(Constants.OrderStatus.PAY_WAIT.getCode())
                .orderId(order.getOrderId())
                .createdAt(order.getCreatedAt())
                .totalAmount(order.getTotalAmount())
                .payType(createOrderReq.getPayType())
                .build();
    }

    @Override
    public QueryHistoryOrderRes queryHistoryOrderByUserId(QueryHistoryOrderReq queryHistoryOrderReq) {
         List<Order> orderList = orderDao.getOrderList(queryHistoryOrderReq.getUserId());
         return QueryHistoryOrderRes.builder()
                 .userId(queryHistoryOrderReq.getUserId())
                 .historyOrders(orderList)
                 .build();
    }

    private boolean subtractProductStock(List<CartItem> cartItems) {
        // 记录库存回滚  商品id 和 购买数量
        Map<Integer, Integer> rollbackMap = stockRollbackHolder.get();
        try{
            //库存扣减
            for(CartItem cartItem : cartItems) {
                //记录库存回滚
                rollbackMap.put(cartItem.getProductId(), cartItem.getQuantity());
                String cacheKey = Constants.RedisKey.PRODUCT_COUNT_KEY + Constants.UNDERLINE + cartItem.getProductId();
                //原子锁扣减   得到扣减后的库存
                Long surplus = redissonService.addAndGet(cacheKey, -cartItem.getQuantity());
                if (surplus < 0) {
                    log.warn("库存不足: productId={}", cartItem.getProductId());
                    throw new AppException(AppExceptionCode.OrderExceptionCode.STOCK_INSUFFICIENT.getCode(),AppExceptionCode.OrderExceptionCode.STOCK_INSUFFICIENT.getInfo());
                }
            }
            //将商品消耗放入延迟队列中消费
            productStockConsumerSendQueue(rollbackMap);
            return true;
        }catch (AppException e) {
            log.error("库存扣减失败: code={}, info={}", e.getCode(), e.getInfo(), e);
            // 回滚已扣减库存
            rollbackStock(rollbackMap);
            return false;
        }
    }

    public void rollbackStock(Map<Integer, Integer> rollbackMap){
        rollbackMap.forEach((productId, quantity) -> {
            String cacheKey = Constants.RedisKey.PRODUCT_COUNT_KEY + Constants.UNDERLINE + productId;
            redissonService.addAndGet(cacheKey, quantity);
        });
    }

    private void productStockConsumerSendQueue(Map<Integer, Integer> stockMap){
        String cacheKey = Constants.RedisKey.PRODUCT_COUNT_KEY;
        RBlockingQueue<Map<Integer, Integer> > blockingQueue = redissonService.getBlockingQueue(cacheKey);
        RDelayedQueue<Map<Integer, Integer> > delayedQueue = redissonService.getDelayedQueue(blockingQueue);
       delayedQueue.offer(stockMap, 5, TimeUnit.MINUTES);
    };



    @Override
    public List<Integer> getOvertimeOrders() {
        return orderDao.getOvertimeOrders();
    }

    @Override
    public void updateOrderStatusToCancelled(int orderId) {
        orderDao.updateOrderStatusToCancelled(orderId);
    }

    @Override
    public Map<Integer, Integer> takeQueue() {
        String cacheKey = Constants.RedisKey.PRODUCT_QUEUE_KEY;
        RBlockingQueue<Map<Integer, Integer>> blockingQueue = redissonService.getBlockingQueue(cacheKey);
        RDelayedQueue<Map<Integer, Integer>> delayedQueue = redissonService.getDelayedQueue(blockingQueue);
        return delayedQueue.poll();
    }


    private void insertOrderItems(Integer orderId, List<CartItem> cartItems) {
        for(CartItem cartItem : cartItems) {
            OrderItems orderItems = new OrderItems();
            orderItems.setOrderId(orderId);
            orderItems.setProductId(cartItem.getProductId());
            orderItems.setQuantity(cartItem.getQuantity());
            orderItems.setPrice(cartItem.getTotalPrice());
            orderItemsDao.insertOrderItems(orderItems);
        }
    }


}
