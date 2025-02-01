package com.qxy.model.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: dawang
 * @Description: 用户历史订单查询请求体
 * @Date: 2025/2/1 20:58
 * @Version: 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryHistoryOrderReq {
    /** 用户id*/
    private Integer userId;

}
