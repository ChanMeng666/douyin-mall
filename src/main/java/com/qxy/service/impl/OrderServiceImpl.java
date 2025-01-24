package com.qxy.service.impl;

import com.qxy.common.constant.Constants;
import com.qxy.model.po.CartItem;
import com.qxy.model.po.Order;
import com.qxy.model.req.CreateOrderReq;
import com.qxy.model.res.OrderRes;
import com.qxy.repository.IOrderItemsRepository;
import com.qxy.repository.IOrderRepository;
import com.qxy.service.IOrderService;
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
public class OrderServiceImpl implements IOrderService {

    @Resource
    private IOrderRepository orderRepository;

    @Resource
    private IOrderItemsRepository orderItemsRepository;

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
        /*Map<Integer, Integer> productIdAndQuantityMap = cartItems.stream().collect(HashMap::new, (m, v) -> m.put(v.getProductId(), v.getQuantity()), HashMap::putAll);
        Set<Integer> productIds = productIdAndQuantityMap.keySet();
         //获取商品信息
        //抛出商品异常*/

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
        orderRepository.createOrder(order);

        //保存订单商品项
        orderItemsRepository.insertOrderItems(order.getOrderId(), cartItems);

        //清除购物车商品

        //减少对应商品库存

        //返回订单响应

        return OrderRes.builder()
                .status(Constants.OrderStatus.CREATE.getCode())
                .orderId(order.getOrderId())
                .totalAmount(order.getTotalAmount())
                .actualAmount(order.getTotalAmount())
                .build();
    }

    @Override
    public Order getOrderList(Integer userId) {
        return orderRepository.getOrderList(userId);
    }
}
