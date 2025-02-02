package com.qxy.model.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: dawang
 * @Description: 订单商品表
 * @Date: 2025/1/20 22:52
 * @Version: 1.0
 */
@Data
public class OrderItems {
    /** 订单项id */
    private Integer orderItemId;
    /** 订单id */
    private Integer orderId;
    /** 商品id */
    private Integer productId;
    /** 商品数量 */
    private Integer quantity;
    /** 商品单价 */
    private BigDecimal price;
    /** 创建时间 */
    private Date createdAt;
    /** 更新时间 */
    private Date updatedAt;
}
