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
    ;

    private String code;
    private String info;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum Login {
        LOGIN_SUCCESS("0003","登录成功"),
        LOGIN_ERROR("0004","登录失败"),
        ;

        private String code;
        private String info;
    }

}
