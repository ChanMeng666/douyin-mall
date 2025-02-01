package com.qxy.model.res;

import com.qxy.model.po.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: dawang
 * @Description: 用户历史订单响应体
 * @Date: 2025/2/1 21:03
 * @Version: 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryHistoryOrderRes {
    /**用户id */
    private Integer userId;
    /**用户历史订单 */
    private List<Order> historyOrders;
}
