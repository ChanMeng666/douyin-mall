package com.qxy.model.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: dawang
 * @Description: 取消订单请求
 * @Date: 2025/2/11 21:56
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelOrderReq {
    /**订单id*/
    private Integer orderId;
    /**用户id*/
    private Integer userId;
}
