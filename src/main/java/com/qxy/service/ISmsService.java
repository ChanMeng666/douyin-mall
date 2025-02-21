package com.qxy.service;

import java.util.Map;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 阿里云短信服务
 */
public interface ISmsService {

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

    /**
     * 核对验证码
     * @param phone
     * @param code
     */
    public void checkCode(String phone, String code);

    /**
     * 获取手机验证码的键
     * @param phone
     * @return 手机验证码的键
     */
    public String getPhoneCodeKey(String phone);

    /**
     * 获取验证码有效时间的键
     * @param phone
     * @return 验证码有效时间的键
     */
    public String getCodeTimeKey(String phone);
}
