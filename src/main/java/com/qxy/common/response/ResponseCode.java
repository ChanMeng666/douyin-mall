package com.qxy.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "操作成功"),
    UN_ERROR("0001", "操作失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    LOGIN_SUCCESS("0003","登录成功"),
    LOGIN_ERROR("0004","登录失败"),
    FAILED_USER_PHONE           ("0004", "输入的手机号有误"),
    FAILED_FREQUENT             ("0005", "操作频繁，请稍后重试"),
    FAILED_TIME_LIMIT           ("0006", "当天请求次数已达到上限"),
    FAILED_SEND_CODE          ("0007", "验证码发送错误"),
    FAILED_INVALID_CODE           ("0008", "验证码无效"),
    FAILED_ERROR_CODE           ("0009", "验证码错误"),
    FAILED_UNREGISTERED           ("0010", "账号未注册"),
    FAILED_USER_EMAIL           ("0011", "输入的邮箱有误"),
    FAILED_VOID_PARAMETER           ("0012", "参数不能为空"),
    ILLEGAL_INPUT_FORMAT          ("0013", "输入格式错误"),
    FAILED_ERROR_PASSWORD          ("0014", "密码错误"),
    FAILED_USER_NOT_EXIST          ("0015", "用户不存在"),
    FAILED_REPEAT_LOGIN          ("0016", "请勿重复登录"),
    FAILED_NOT_LOGIN          ("0017", "您还未登录"),
    FAILED_USER_EXIST          ("0018", "用户已存在"),
    FAILED_SIGN_OUT          ("0019", "注销失败"),
    FAILED_PASSWORD_REUSE          ("0020", "新密码不能与原密码相同"),
    STOCK_INSUFFICIENT("STOCK_INSUFFICIENT", "库存不足"),
    FAILED_GOODS_NOT_EXISTS("GOODS_NOT_EXISTS","商品不存在或已下架"),

    ;
    private String code;
    private String info;
}
