package com.qxy.controller.dto.order;

import com.qxy.model.po.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: dawang
 * @Description: 订单请求创建请求传输对象
 * @Date: 2025/2/1 23:03
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequestDto {
    /** 用户ID */
    private Integer userId;
    /** 购物车ID */
    private Integer cartId;
    /** 购物车商品列表 */
    private List<CartItem> cartItems;
    /** 支付方式 */
    private String payType;
}
