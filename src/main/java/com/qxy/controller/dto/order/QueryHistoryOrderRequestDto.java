package com.qxy.controller.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: dawang
 * @Description: 历史订单请求传输对象
 * @Date: 2025/2/10 22:20
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryHistoryOrderRequestDto {
    /** 用户id*/
    private Integer userId;
}
