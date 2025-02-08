package com.qxy.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author: dawang
 * @Description: 运行异常码
 * @Date: 2025/2/8 23:27
 * @Version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AppExceptionCode {
    private String code;
    private String info;
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum OrderExceptionCode{
        STOCK_INSUFFICIENT("stock_insufficient", "库存不足"),
        ;
        private String code;
        private String info;
    }
}
