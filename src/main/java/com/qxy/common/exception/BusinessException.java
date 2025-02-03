// com.qxy.common.exception.BusinessException.java
package com.qxy.common.exception;

/**
 * @author water
 * @description: 自定义业务异常
 * @date 2025/2/3 20:15
 * @version 1.0
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}