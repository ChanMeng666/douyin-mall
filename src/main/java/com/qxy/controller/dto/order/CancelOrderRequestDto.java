package com.qxy.controller.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: dawang
 * @Description: 取消订单请求dto
 * @Date: 2025/2/11 21:41
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelOrderRequestDto {
    /**订单id*/
    private Integer orderId;
    /**用户id*/
    private Integer userId;
}
