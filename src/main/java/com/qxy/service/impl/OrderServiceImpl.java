package com.qxy.service.impl;

import com.qxy.common.constant.Constants;
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
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    private ProductServiceImpl productService;

    @Resource
    private RedissonService redissonService;
    /**
     * 创建订单
     *
     * @param createOrderReq 创建订单请求
     * @return 订单响应
     */
    @Override
    public CreateOrderRes createOrder(CreateOrderReq createOrderReq) {
        log.info("创建订单请求: userId:{} , cartId:{} , payType:{}", createOrderReq.getUserId(),createOrderReq.getCartId(), createOrderReq.getPayType());
        Order order = new Order();
        // 获取购物车商品信息
        List<CartItem> cartItems = createOrderReq.getCartItems();

        // 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            totalAmount = totalAmount.add(cartItem.getTotalPrice().multiply(new BigDecimal(cartItem.getQuantity())));
        }
        order.setTotalAmount(totalAmount);

        //设置订单的支付类型
        order.setPayType(createOrderReq.getPayType());

        //设置订单状态
        order.setStatus(Constants.OrderStatus.CREATE.getCode());

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
        //减少对应商品库存
        boolean stockStatus = subtractProductStock(cartItems);
        //库存不足
        if (!stockStatus) {
            return null;
        }

        //返回订单响应
        return CreateOrderRes.builder()
                .status(Constants.OrderStatus.CREATE.getCode())
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
        //库存扣减
        for(CartItem cartItem : cartItems) {
            //将商品库存存入缓存中
            productService.storeProductStock(cartItem.getProductId());
            String cacheKey = Constants.RedisKey.PRODUCT_COUNT_KEY + Constants.UNDERLINE + cartItem.getProductId();
            //原子锁扣减
            long surplus = redissonService.decr(cacheKey);
            if (surplus < 0) {
                redissonService.setAtomicLong(cacheKey, 0);
                return false;
            }
            String lockKey = cacheKey + Constants.UNDERLINE + surplus;
            Boolean lock = redissonService.setNx(lockKey);
            if(!lock) {
                log.info("商品库存加锁失败 productId:{}", cartItem.getProductId());
                return false;
            }
        }
        //将商品消耗放入延迟队列中消费
        productStockConsumerSendQueue(cartItems);
        return true;
    }

    private void productStockConsumerSendQueue(List<CartItem> cartItems){
        String cacheKey = Constants.RedisKey.PRODUCT_COUNT_KEY;
        RBlockingQueue<CartItem> blockingQueue = redissonService.getBlockingQueue(cacheKey);
        RDelayedQueue<CartItem> delayedQueue = redissonService.getDelayedQueue(blockingQueue);
        for(CartItem cartItem: cartItems) {
            delayedQueue.offer(cartItem,5, TimeUnit.SECONDS);
        }
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
    public CartItem takeQueue() {
        String cacheKey = Constants.RedisKey.PRODUCT_QUEUE_KEY;
        RBlockingQueue<CartItem> blockingQueue = redissonService.getBlockingQueue(cacheKey);
        RDelayedQueue<CartItem> delayedQueue = redissonService.getDelayedQueue(blockingQueue);
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

    private Integer generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Long atomicLong = redissonService.getAtomicLong("order:seq");
        Integer sequence = atomicLong.intValue();
        return sequence;
    }
}
