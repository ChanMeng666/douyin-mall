package com.qxy.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
public class Constants {

    public final static String SPLIT = ",";

    public final static String COLON = ":";
    public final static String SPACE = " ";
    public final static String UNDERLINE = "_";
    public final static String DEFAULT_CODE = "123456";
    public final static String ACCOUNT_CODE_KEY = "a:c:";
    public final static String CODE_TIME_KEY = "c:t:";
    public final static Integer ROLE_ADMIN = 1;
    public final static Integer ROLE_USER = 2;
    public final static Integer ROLE_MERCHANT = 3;


//    /**
//     * 定义出缓存key的前缀标识，
//     */
//    public static class RedisKey {
//
//
//    }

    /**
     * Redis Key前缀
     */
    public static class RedisKey {
        public static final String TEST_PREFIX = "test:";
        public static final String USER_PREFIX = "user:";
        public static final String PRODUCT_PREFIX = "product:";
        public static final String ORDER_PREFIX = "order:";
        public static final String PRODUCT_COUNT_KEY = "product_count_key";

        public static final String PRODUCT_QUEUE_KEY = "product_queue_Key";
        // 生成带前缀的key
        public static String getKey(String prefix, String key) {
            return prefix + key;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum OrderStatus {
        HAVE_PAID("have_paid","支付完成 - 订单已被支付"),
        PAY_WAIT("pay_wait", "等待支付 - 订单创建完成后，创建支付单"),
        CANCELLED("cancelled","订单取消状态-订单已被用户取消或订单已超时取消");
        private final String code;
        private final String desc;
    }

}
