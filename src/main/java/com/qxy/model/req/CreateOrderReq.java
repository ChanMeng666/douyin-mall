package com.qxy.model.req;

import com.qxy.model.po.CartItem;
import lombok.Data;

import java.util.List;

/**
 * @Author: dawang
 * @Description: 创建订单的请求体
 * @Date: 2025/1/18 22:37
 * @Version: 1.0
 */
@Data
public class CreateOrderReq {
    /** 用户ID */
    private Integer userId;
    /** 购物车ID */
    private Integer cartId;
    /** 购物车商品列表 */
    private List<CartItem> cartItems;
    /** 支付方式 */
    private String payType;
}
