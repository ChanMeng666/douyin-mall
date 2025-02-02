package com.qxy.model.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: dawang
 * @Description: TODO
 * @Date: 2025/1/18 23:01
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRes {
    /** 订单id */
    private Integer orderId;
    /** 订单总金额 */
    private BigDecimal totalAmount;
    /** 订单状态   1.pay_wait【未付款】  2.paid【支付完成】 3.Cancelled【取消】*/
    private String status;
    /** 支付方式  1.【支付宝】  2.【微信】*/
    private String payType;
    /** 订单创建时间*/
    private Date createdAt;
}
