package com.qxy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.qxy.common.constant.Constants;
import com.qxy.common.exception.BusinessException;
import com.qxy.common.response.ResponseCode;
import com.qxy.infrastructure.redis.IRedisService;
import com.qxy.service.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 阿里云短信服务实现
 */
@Component
@Slf4j
public class SmsServiceImpl implements ISmsService {
    @Autowired
    private IRedisService redisService;
    @Autowired
    private Client aliClient;
    //业务配置
    @Value("${sms.aliyun.templateCode:}")
    private String templateCode;
    @Value("${sms.send-message:true}")
    private boolean sendMessage;
    @Value("${sms.aliyun.sign-name:}")
    private String signName;

    /**
     * 发送短信验证码
     *
     * @param phone
     * @param code
     */
    @Override
    public boolean sendMobileCode(String phone, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        return sendTempMessage(phone, templateCode, params);
    }

    /**
     * 发送模板消息
     *
     * @param phone
     * @param templateCode
     * @param params
     */
    @Override
    public boolean sendTempMessage(String phone, String templateCode, Map<String, String> params) {
        if (!sendMessage) {
            log.error("短信发送通道关闭，发送失败......" + phone);
            return false;
        }
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phone);
        sendSmsRequest.setSignName(signName);
        sendSmsRequest.setTemplateCode(templateCode);
        sendSmsRequest.setTemplateParam(JSON.toJSONString(params));
        try {
            SendSmsResponse sendSmsResponse = aliClient.sendSms(sendSmsRequest);
            SendSmsResponseBody responseBody = sendSmsResponse.getBody();
            if (!"OK".equalsIgnoreCase(responseBody.getCode())) {
                log.error("短信{} 发送失败，失败原因:{}.... ",
                        JSON.toJSONString(sendSmsRequest), responseBody.getMessage());
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("短信{} 发送失败，失败原因:{}.... ",
                    JSON.toJSONString(sendSmsRequest), e.getMessage());
            return false;
        }
    }

    /**
     * 核对验证码
     * @param phone
     * @param code
     */
    @Override
    public void checkCode(String phone, String code) {
        String phoneCodeKey = getPhoneCodeKey(phone);
        String cacheCode = redisService.getValue(phoneCodeKey);
        if (StrUtil.isEmpty(cacheCode)) {
            throw new BusinessException(ResponseCode.FAILED_INVALID_CODE);
        }
        if (!cacheCode.equals(code)) {
            throw new BusinessException(ResponseCode.FAILED_ERROR_CODE);
        }
        //验证码比对成功

    }

    /**
     * 获取手机验证码的键
     * @param phone
     * @return 手机验证码的键
     */
    @Override
    public String getPhoneCodeKey(String phone) {
        return Constants.PHONE_CODE_KEY + phone;
    }

    /**
     * 获取验证码有效时间的键
     * @param phone
     * @return 验证码有效时间的键
     */
    @Override
    public String getCodeTimeKey(String phone) {
        return Constants.CODE_TIME_KEY + phone;
    }

}
