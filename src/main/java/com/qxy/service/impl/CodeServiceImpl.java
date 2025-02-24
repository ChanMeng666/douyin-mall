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
import com.qxy.service.ICodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.commons.mail.HtmlEmail;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 阿里云短信服务实现
 */
@Component
@Slf4j
public class CodeServiceImpl implements ICodeService {
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
    @Value("${emailCode.HostName:}")
    private String HostName;
    @Value("${emailCode.Charset:}")
    private String Charset;
    @Value("${emailCode.emailAccount:}")
    private String emailAccount;
    @Value("${emailCode.emailSender:}")
    private String emailSender;
    @Value("${emailCode.AuthPassword:}")
    private String AuthPassword;
    @Value("${emailCode.Code-expiration:}")
    private Long expiration;
    @Value("${emailCode.SSLOnConnect:}")
    private boolean SSLOnConnect;

    /**
     * 发送短信验证码
     *
     * @param phone
     * @param code
     */
    @Override
    public boolean sendPhoneCode(String phone, String code) {
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
     * 发送邮箱验证码
     * @param email
     * @param code
     */
    @Override
    public boolean sendEmailCode(String email, String code){
        if( email==null || code==null || email.equals("") || code.equals("")) return false;
        HtmlEmail mail = new HtmlEmail();
        try {
            /*发送邮件的服务器 126邮箱为smtp.126.com,163邮箱为163.smtp.com，QQ为smtp.qq.com*/
            mail.setHostName(HostName);
            /*不设置发送的消息有可能是乱码*/
            mail.setCharset(Charset);
            /*IMAP/SMTP服务的密码 username为你开启发送验证码功能的邮箱号 password为你在qq邮箱获取到的一串字符串*/
            mail.setAuthentication(emailAccount, AuthPassword);
            /*发送邮件的邮箱和发件人*/
            mail.setFrom(emailAccount, emailSender);
            /*使用安全链接*/
            mail.setSSLOnConnect(SSLOnConnect);
            /*接收的邮箱*/
            mail.addTo(email);
            /*设置邮件的主题*/
            mail.setSubject("DouyinMall验证码");
            String MSG = "DDDD      OOO     U   U    Y   Y   III   N   N       M   M     AAA     L        L  \n" +
                         "D   D    O   O    U   U     Y Y     I    NN  N       MM MM    A   A    L        L  \n" +
                         "D   D    O   O    U   U      Y      I    N N N  ---  M M M    AAAAA    L        L  \n" +
                         "D   D    O   O    U   U      Y      I    N  NN       M   M    A   A    L        L  \n" +
                         "DDDD      OOO      UUU       Y     III   N   N       M   M    A   A    LLLLL    LLLLL\n"+
                         "\n尊敬的用户:您的验证码: " + code + " , 有效期为 "+expiration+" 分钟"+" , 您正在进行注册/登录验证，请勿泄露与他人!";
            /*设置邮件的内容*/
            mail.setMsg(MSG);
            mail.send();//发送
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("邮箱验证码{}发送失败,失败原因:{}",code,e.getMessage());
            return false;
        }
    }

    /**
     * 核对验证码
     * @param account
     * @param code
     */
    @Override
    public void checkCode(String account, String code) {
        String accountCodeKey = getAccountCodeKey(account);
        String cacheCode = redisService.getValue(accountCodeKey);
        if (StrUtil.isEmpty(cacheCode)) {
            throw new BusinessException(ResponseCode.FAILED_INVALID_CODE);
        }
        if (!cacheCode.equals(code)) {
            throw new BusinessException(ResponseCode.FAILED_ERROR_CODE);
        }
    }

    /**
     * 获取手机或邮箱验证码的键
     * @param account
     * @return 手机或邮箱验证码的键
     */
    @Override
    public String getAccountCodeKey(String account) {
        return Constants.ACCOUNT_CODE_KEY + account;
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
