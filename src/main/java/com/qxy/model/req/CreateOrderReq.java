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
    private Long userId;            // 用户ID
    private List<CartItem> cartItems;  // 购物车项列表
    private String payType;      // 支付方式，例如 "alipay", "wechat"
}
