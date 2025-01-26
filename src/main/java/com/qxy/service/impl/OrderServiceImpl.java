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
import com.qxy.model.res.OrderRes;
import com.qxy.service.IOrderService;
import com.qxy.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


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
    public OrderRes createOrder(CreateOrderReq createOrderReq) {
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
        return OrderRes.builder()
                .status(Constants.OrderStatus.CREATE.getCode())
                .orderId(order.getOrderId())
                .totalAmount(order.getTotalAmount())
                .actualAmount(order.getTotalAmount())
                .build();
    }

    private boolean subtractProductStock(List<CartItem> cartItems) {
        for(CartItem cartItem : cartItems) {
            productService.storeProductStock(cartItem.getProductId());
            String cacheKey = Constants.RedisKey.PRODUCT_COUNT_KEY + Constants.UNDERLINE + cartItem.getProductId();
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
        return true;
    }

    @Override
    public Order getOrderList(Integer userId) {
        return orderDao.getOrderList(userId);
    }

    @Override
    public List<Integer> getOvertimeOrders() {
        return orderDao.getOvertimeOrders();
    }

    @Override
    public void updateOrderStatusToCancelled(int orderId) {
        orderDao.updateOrderStatusToCancelled(orderId);
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
