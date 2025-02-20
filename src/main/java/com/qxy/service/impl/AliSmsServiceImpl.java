package com.qxy.service.impl;

import com.alibaba.fastjson2.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.google.gson.Gson;
import com.qxy.service.IAliSmsService;
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
public class AliSmsServiceImpl implements IAliSmsService {
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

}
