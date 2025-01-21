package com.qxy.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
public class Constants {

    public final static String SPLIT = ",";

    public final static String COLON = ":";
    public final static String SPACE = " ";
    public final static String UNDERLINE = "_";

    /**
     * 定义出缓存key的前缀标识，
     */
    public static class RedisKey {


    }
    @Getter
    @AllArgsConstructor
    public enum OrderStatus {
        CREATE("CREATE","订单创建状态-如果订单"),
        PAY_WAIT("PAY_WAIT", "等待支付 - 订单创建完成后，创建支付单"),
        CANCELLED("CANCELLED","订单取消状态-订单已被用户取消或订单已超时取消")
        ;
        private final String code;
        private final String desc;
    }

}
