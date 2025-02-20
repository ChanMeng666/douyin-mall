// com.qxy.common.exception.BusinessException.java
package com.qxy.common.exception;

import com.qxy.common.response.ResponseCode;
import lombok.Getter;

/**
 * @author water
 * @description: 自定义业务异常
 * @date 2025/2/3 20:15
 * @version 1.0
 */
@Getter
public class BusinessException extends RuntimeException {
    private ResponseCode responseCode;
    private String msg;
    public BusinessException(ResponseCode responseCode,String msg) {
        this.responseCode = responseCode;
        this.msg = msg;
    }
    public BusinessException(String msg) {
        this.msg = msg;
    }
    public BusinessException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }
}