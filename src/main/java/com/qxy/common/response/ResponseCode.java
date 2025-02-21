package com.qxy.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "调用成功"),
    UN_ERROR("0001", "调用失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    LOGIN_SUCCESS("0003","登录成功"),
    LOGIN_ERROR("0004","登录失败"),
    FAILED_USER_PHONE           ("0004", "你输入的手机号有误"),
    FAILED_FREQUENT             ("0005", "操作频繁，请稍后重试"),
    FAILED_TIME_LIMIT           ("0006", "当天请求次数已达到上限"),
    FAILED_SEND_CODE          ("0007", "验证码发送错误"),
    FAILED_INVALID_CODE           ("0008", "验证码无效"),
    FAILED_ERROR_CODE           ("0009", "验证码错误"),
    FAILED_UNREGISTERED           ("0010", "账号未注册"),
    STOCK_INSUFFICIENT("STOCK_INSUFFICIENT", "库存不足"),
    FAILED_GOODS_NOT_EXISTS("GOODS_NOT_EXISTS","商品不存在或已下架"),

    ;
    private String code;
    private String info;
}
