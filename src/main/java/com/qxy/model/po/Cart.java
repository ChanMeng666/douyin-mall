package com.qxy.model.po;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class Cart {
    /** 购物车ID */
    private Integer cartId;

    /** 用户ID */
    private Integer userId;

    /** 购物车商品列表 */
    private List<CartItem> cartItems;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}