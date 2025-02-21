package com.qxy.service;

import java.util.Map;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 阿里云短信服务
 */
public interface ICodeService {

    /**
     * 发送短信验证码
     *
     * @param phone
     * @param code
     */
    public boolean sendPhoneCode(String phone, String code);

    /**
     * 发送邮箱验证码
     * @param email
     * @param code
     */
    public boolean sendEmailCode(String email, String code);

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
     * @param account
     * @param code
     */
    public void checkCode(String account, String code);

    /**
     * 获取手机或邮箱验证码的键
     * @param account
     * @return 手机或邮箱验证码的键
     */
    public String getAccountCodeKey(String account);

    /**
     * 获取验证码有效时间的键
     * @param phone
     * @return 验证码有效时间的键
     */
    public String getCodeTimeKey(String phone);
}
