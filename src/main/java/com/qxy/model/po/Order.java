package com.qxy.model.po;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: dawang
 * @Description: 订单表
 * @Date: 2025/1/18 19:39
 * @Version: 1.0
 */
@Data
public class Order {
    /** 订单Id*/
    private Integer orderId;
    /** 用户Id*/
    private Integer userId;
    /** 订单状态 1.pay_wait【未付款】 2.pay_unshipped【已支付，未发货】 3.shipped【已发货】 4.Cancelled【取消】*/
    private String status;
    /** 订单总价*/
    private BigDecimal totalAmount;
    /** 支付方式  1.【支付宝】  2.【微信】*/
    private String payType;
    /** 订单支付时间*/
    private Date payTime;
    /** 订单创建时间*/
    private Date createTime;
    /** 更新时间*/
    private Date updateTime;
}
