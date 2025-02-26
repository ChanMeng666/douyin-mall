package com.qxy.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.core.util.RandomUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson2.JSON;
import com.qxy.common.constant.Constants;
import com.qxy.common.exception.BusinessException;
import com.qxy.common.response.ResponseCode;
import com.qxy.common.tool.Validator;
import com.qxy.controller.dto.User.*;
import com.qxy.dao.UserDao;
import com.qxy.infrastructure.redis.IRedisService;
import com.qxy.model.po.User;
import com.qxy.service.ICodeService;
import com.qxy.service.IUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 用户服务实现
 */
@Service("UserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private ICodeService codeService;
    @Value("${sms.Code-expiration:}")
    private Long phoneCodeExpiration;
    @Value("${emailCode.Code-expiration:}")
    private Long emailCodeExpiration;
    @Value("${sms.send-limit:}")
    private Integer SMS_sendLimit;
    @Value("${emailCode.send-limit:}")
    private Integer emailCode_sendLimit;
    @Value("${sms.is-send:}")
    private boolean SMS_isSend;  //SMS开关打开：true  开关关闭false
    @Value("${emailCode.is-send:}")
    private boolean emailCode_isSend;  //emailCode开关打开：true  开关关闭false

    @Override
    public Boolean Login(LoginDTO logindto, boolean isRemember, boolean isSevenDays){
        if(!StpUtil.isLogin()) {
            String loginId = logindto.getAccount();
            String password = logindto.getPassword();
            String device = logindto.getLoginDevice();
            if(loginId==null||password==null||loginId.equals("")||password.equals(""))
                throw new BusinessException(ResponseCode.FAILED_VOID_PARAMETER);
            String str = Validator.getKindOfAccount(loginId);
            if(str.equals(""))
                throw new BusinessException(ResponseCode.ILLEGAL_INPUT_FORMAT);
            User userinfo =userDao.getUserInfoByLoginId(loginId);
            if(userinfo!=null){
                String pw = userinfo.getPassword();
                password = SaSecureUtil.sha256(password);
                if(pw.equals(password)) {
                    SaLoginModel saLoginModel = new SaLoginModel()
                            .setDevice(device)
                            .setIsLastingCookie(isRemember);
                    if(isSevenDays) saLoginModel.setTimeout(86400*7);
                    StpUtil.login(loginId,saLoginModel);
                    return true;
                }else throw new BusinessException(ResponseCode.FAILED_ERROR_PASSWORD);
            }
            else throw new BusinessException(ResponseCode.FAILED_USER_NOT_EXIST, str+"未注册");
        }
        else throw new BusinessException(ResponseCode.FAILED_REPEAT_LOGIN, StpUtil.getTokenInfo());
    }

    @Override
    public Boolean LoginByCode(LoginByCodeDTO loginByCodedto, boolean isRemember, boolean isSevenDays){
        if(!StpUtil.isLogin()) {
            String account = loginByCodedto.getAccount();
            String code = loginByCodedto.getCode();
            String device = loginByCodedto.getLoginDevice();
            String accountCodeKey = codeService.getAccountCodeKey(account);
            if(code==null||account==null||code.equals("")||account.equals(""))
                throw new BusinessException(ResponseCode.FAILED_VOID_PARAMETER);
            User user = userDao.getUserInfoByLoginId(account);
            String kindOfAccount = Validator.getKindOfAccount(account);
            if(kindOfAccount.equals("")||kindOfAccount.equals("用户名"))
                throw new BusinessException(ResponseCode.ILLEGAL_INPUT_FORMAT);
            if(user == null)
                throw new BusinessException(ResponseCode.FAILED_USER_NOT_EXIST, kindOfAccount+"未注册");
            //核对验证码
            codeService.checkCode(account, code);
            SaLoginModel saLoginModel = new SaLoginModel()
                                        .setDevice(device)
                                        .setIsLastingCookie(isRemember);
            if(isSevenDays) saLoginModel.setTimeout(86400*7);
            //核对成功，则登录
            StpUtil.login(account,saLoginModel);
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            //登录成功，则删除缓存的验证码
            redisService.remove(accountCodeKey);
            return true;
        }
        else throw new BusinessException(ResponseCode.FAILED_REPEAT_LOGIN, StpUtil.getTokenInfo());
    }

    @Override
    public Boolean Logout(){
        if(StpUtil.isLogin()) {
            StpUtil.logout(StpUtil.getLoginId(),StpUtil.getLoginDevice());
            return true;
        }
        else throw new BusinessException(ResponseCode.FAILED_NOT_LOGIN);
    }

    @Transactional
    @Override
    public Boolean SignUp(@NotNull SignUpDTO signupdto, Integer RoleId){
        String username = signupdto.getUserName();
        String password = signupdto.getPassword();
        String phone = signupdto.getPhone();
        String email = signupdto.getEmail();
        String code = signupdto.getCode();
        String accountCodeKey = codeService.getAccountCodeKey(email);
        if(username==null||phone==null||username.equals("")||phone.equals(""))
            throw new BusinessException(ResponseCode.FAILED_VOID_PARAMETER);
        if(!Validator.isValidUsername(username)||!Validator.isValidPassword(password)
                ||!Validator.isValidPhoneNumber(phone))
            throw new BusinessException(ResponseCode.ILLEGAL_INPUT_FORMAT);

        if(userDao.getUserInfoByUserName(username)!=null)
            throw new BusinessException(ResponseCode.FAILED_USER_EXIST,"用户名已存在");
        else if (userDao.getUserInfoByPhone(phone)!=null)
            throw new BusinessException(ResponseCode.FAILED_USER_EXIST,"手机号已注册");
        else if (userDao.getUserInfoByEmail(email)!=null)
            throw new BusinessException(ResponseCode.FAILED_USER_EXIST,"邮箱已注册");
        else {
            //核对验证码
            codeService.checkCode(email,code);
            //核对成功则允许注册
            User user = new User();
            user.setUserName(username);
            password = SaSecureUtil.sha256(password);
            user.setPassword(password);
            user.setPhone(phone);
            user.setEmail(email);
            userDao.createUser(user,RoleId);
            //用户创建成功，则删除缓存的验证码
            redisService.remove(accountCodeKey);
            return true;
        }
    }

    @Override
    public Boolean SignOut(){
        if(!StpUtil.isLogin())
            throw new BusinessException(ResponseCode.FAILED_NOT_LOGIN);
        try{
            userDao.deleteUserByLoginId(StpUtil.getLoginId().toString());
            StpUtil.logout();
        }catch (Exception e){
            throw new BusinessException(ResponseCode.FAILED_SIGN_OUT);
        }
        return true;
    }

    @Override
    public boolean sendPhoneCode(@NotNull SendSMSCodeDTO sendSMSCodedto) {
        String phone = sendSMSCodedto.getPhone();
        if (!Validator.isValidPhoneNumber(phone)) {
            throw new BusinessException(ResponseCode.FAILED_USER_PHONE);
        }
        String phoneCodeKey = codeService.getAccountCodeKey(phone);
        Long expire = redisService.getExpire(phoneCodeKey, TimeUnit.SECONDS);
        if (expire != null && (phoneCodeExpiration * 60 - expire) < 60 ){
            throw new BusinessException(ResponseCode.FAILED_FREQUENT);
        }
        String codeTimeKey = codeService.getCodeTimeKey(phone);
        Long sendTimes = redisService.getAtomicLong(codeTimeKey);
        if (sendTimes != 0 && sendTimes >= SMS_sendLimit) {
            throw new BusinessException(ResponseCode.FAILED_TIME_LIMIT);
        }
        String code = SMS_isSend ? RandomUtil.randomNumbers(6) : Constants.DEFAULT_CODE;
        //存储到redis  数据结构：String  key：p:c:手机号  value :code
        redisService.setValue(phoneCodeKey, code, phoneCodeExpiration, TimeUnit.MINUTES);
        if (SMS_isSend) {
            boolean sendMobileCode = codeService.sendPhoneCode(phone, code);
            if (!sendMobileCode) {
                throw new BusinessException(ResponseCode.FAILED_SEND_CODE);
            }
        }
        redisService.incr(codeTimeKey);
        if (sendTimes == 0) {  //说明是当天第一次发起获取验证码的请求
            long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
            redisService.setExpire(codeTimeKey, seconds, TimeUnit.SECONDS);
        }
        return true;
    }

    @Override
    public boolean SendEmailCode(@NotNull SendEmailCodeDTO sendEmailCodedto){
        String email = sendEmailCodedto.getEmail();
        if (!Validator.isValidEmail(email)) {
            throw new BusinessException(ResponseCode.FAILED_USER_EMAIL);
        }
        String emailCodeKey = codeService.getAccountCodeKey(email);
        Long expire = redisService.getExpire(emailCodeKey, TimeUnit.SECONDS);
        if (expire != null && (emailCodeExpiration * 60 - expire) < 60 ){
            throw new BusinessException(ResponseCode.FAILED_FREQUENT);
        }
        String codeTimeKey = codeService.getCodeTimeKey(email);
        Long sendTimes = redisService.getAtomicLong(codeTimeKey);
        if (sendTimes != 0 && sendTimes >= emailCode_sendLimit) {
            throw new BusinessException(ResponseCode.FAILED_TIME_LIMIT);
        }
        String code = emailCode_isSend ? RandomUtil.randomNumbers(6) : Constants.DEFAULT_CODE;
        //存储到redis  数据结构：String  key：p:c:手机号  value :code
        redisService.setValue(emailCodeKey, code, emailCodeExpiration, TimeUnit.MINUTES);
        if (emailCode_isSend) {
            boolean sendEmailCode = codeService.sendEmailCode(email, code);
            if (!sendEmailCode) {
                throw new BusinessException(ResponseCode.FAILED_SEND_CODE);
            }
        }
        redisService.incr(codeTimeKey);
        if (sendTimes == 0) {  //说明是当天第一次发起获取验证码的请求
            long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
            redisService.setExpire(codeTimeKey, seconds, TimeUnit.SECONDS);
        }
        return true;
    }

    @Override
    public User getInfoByLoginId(String loginId){
        return userDao.getUserInfoByLoginId(loginId);
    }

}
