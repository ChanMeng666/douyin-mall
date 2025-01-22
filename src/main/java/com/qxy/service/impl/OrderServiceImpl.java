package com.qxy.service.impl;

import com.qxy.common.constant.Constants;
import com.qxy.model.po.CartItem;
import com.qxy.model.po.Order;
import com.qxy.model.req.CreateOrderReq;
import com.qxy.model.res.OrderRes;
import com.qxy.repository.IOrderItemsRepository;
import com.qxy.repository.IOrderRepository;
import com.qxy.service.IOrderService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: dawang
 * @Description: 订单服务实现类
 * @Date: 2025/1/18 22:14
 * @Version: 1.0
 */
public class OrderServiceImpl implements IOrderService {

    @Resource
    private IOrderRepository orderRepository;

    @Resource
    private IOrderItemsRepository orderItemsRepository;
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
            totalAmount = totalAmount.add(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
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
//        orderItemsRepository.createOrderItems(order.getOrderId(), cartItems);

        return null;
    }
}
