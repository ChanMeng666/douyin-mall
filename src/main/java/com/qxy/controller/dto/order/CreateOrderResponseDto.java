package com.qxy.controller.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: dawang
 * @Description: 订单创建数据传输返回对象
 * @Date: 2025/2/1 23:04
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderResponseDto {
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
