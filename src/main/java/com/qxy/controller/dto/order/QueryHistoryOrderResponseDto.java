package com.qxy.controller.dto.order;

import com.qxy.model.po.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: dawang
 * @Description: 历史订单数据传输返回对象
 * @Date: 2025/2/10 22:20
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryHistoryOrderResponseDto {
    /**用户id */
    private Integer userId;
    /**用户历史订单 */
    private List<Order> historyOrders;
}
