package com.qxy.service;

import java.util.Map;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 阿里云短信服务
 */
public interface IAliSmsService {

    /**
     * 发送短信验证码
     *
     * @param phone
     * @param code
     */
    public boolean sendMobileCode(String phone, String code);

    /**
     * 发送模板消息
     *
     * @param phone
     * @param templateCode
     * @param params
     */
    public boolean sendTempMessage(String phone, String templateCode,
                                   Map<String, String> params);
}
