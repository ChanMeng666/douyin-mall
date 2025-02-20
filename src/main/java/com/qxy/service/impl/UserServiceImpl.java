package com.qxy.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;
import com.qxy.common.constant.Constants;
import com.qxy.common.exception.BusinessException;
import com.qxy.common.response.ResponseCode;
import com.qxy.common.tool.Validator;
import com.qxy.controller.dto.User.LoginByCodeDTO;
import com.qxy.controller.dto.User.LoginDTO;
import com.qxy.controller.dto.User.SignUpDTO;
import com.qxy.dao.UserDao;
import com.qxy.infrastructure.redis.IRedisService;
import com.qxy.model.po.User;
import com.qxy.service.IAliSmsService;
import com.qxy.service.IUserService;
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
 * @description: TODO
 */
@Service("UserService")
public class UserServiceImpl implements IUserService {
//    private final UserDao userDao;
//
//    public UserServiceImpl(UserDao userDao) {
//        this.userDao = userDao;
//    }
    @Autowired
    private UserDao userDao;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private IAliSmsService aliSmsService;
    @Value("${sms.code-expiration:}")
    private Long phoneCodeExpiration;

    @Value("${sms.send-limit:}")
    private Integer sendLimit;

    @Value("${sms.is-send:}")
    private boolean isSend;  //开关打开：true  开关关闭false

    @Override
    public SaResult Login(LoginDTO logindto){
        if(!StpUtil.isLogin()) {
            String loginId = logindto.getLoginId();
            String password = logindto.getPassword();
            if(loginId.equals("") || password.equals("")) return new SaResult(401,"参数不能为空",null);
            String str = "";
            if(Validator.isValidEmail(loginId)) str = "邮箱";
            else if(Validator.isValidPhoneNumber(loginId)) str = "手机号";
            else if(Validator.isValidUsername(loginId)) str = "用户名";
            else return new SaResult(401, "输入账号格式错误", null);
            User userinfo =userDao.getUserInfoByLoginId(loginId);
            if(userinfo!=null){
                String pw = userinfo.getPassword();
                if(pw.equals(password)) {
                    StpUtil.login(loginId);
                    SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
                    return new SaResult(200, str+"登录成功", tokenInfo);
                }else return new SaResult(401, "密码错误，登录失败", null);
            }
            else return new SaResult(401, str+"不存在", null);
        }
        else return new SaResult(200,"已登录，请勿重复登录",StpUtil.getTokenInfo());
    }

    @Override
    public SaResult LoginBySMSCode(LoginByCodeDTO loginByCodedto){
        if(!StpUtil.isLogin()) {
            String phone = loginByCodedto.getPhone();
            String code = loginByCodedto.getCode();
            String phoneCodeKey = getPhoneCodeKey(phone);
            if(code==null || phone==null) return new SaResult(401,"参数不能为空",null);
            User user = userDao.getUserInfoByLoginId(phone);
            if(user == null) return new SaResult(401, "手机号还未注册", null);
            //核对验证码
            checkCode(phone, code);
            //核对成功，则登录
            StpUtil.login(phone);
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            //登录成功，则删除缓存的验证码
            redisService.remove(phoneCodeKey);
            return new SaResult(200, "手机验证码登录成功", tokenInfo);
        }
        else return new SaResult(200,"已登录，请勿重复登录",StpUtil.getTokenInfo());
    }

    @Override
    public SaResult Logout(){
        if(StpUtil.isLogin()) {
            StpUtil.logout();
            return new SaResult(200,"成功退出登录",null);
        }
        else return new SaResult(401,"您还未登录",null);
    }

    @Transactional
    @Override
    public SaResult SignUp(SignUpDTO signupdto){
        String username = signupdto.getUserName();
        String password = signupdto.getPassword();
        String phone = signupdto.getPhone();
        String code = signupdto.getCode();
        String phoneCodeKey = getPhoneCodeKey(phone);
        if(username==null || phone==null) return new SaResult(401,"参数不能为空",null);
        if(!Validator.isValidUsername(username)) return new SaResult(401,"用户名格式错误",null);
        else if(!Validator.isValidPassword(password)) return new SaResult(401,"密码格式错误",null);
        else if(!Validator.isValidPhoneNumber(phone)) return new SaResult(401,"手机号格式错误",null);
        //未注册
        if(userDao.getUserInfoByUserName(username)!=null)
            return new SaResult(200,"用户名已存在",username);
        else if (userDao.getUserInfoByPhone(phone)!=null)
            return new SaResult(200,"手机号已注册",phone);
        else {
            //核对验证码
            checkCode(phone,code);
            //核对成功则允许注册
            User user = new User();
            user.setUserName(username);
            user.setPassword(password);
            user.setPhone(phone);
            userDao.createUser(user,Constants.ROLE_USER);
            //用户创建成功，则删除缓存的验证码
            redisService.remove(phoneCodeKey);
            return new SaResult(200,"注册成功",user);
        }
    }

    @Override
    public SaResult SignOut(){
        if(!StpUtil.isLogin()) return new SaResult(401,"您还未登录！",null);
        try{
            userDao.deleteUserByLoginId(StpUtil.getLoginId().toString());
            StpUtil.logout();
        }catch (Exception e){
            return new SaResult(401,"注销失败",e.getMessage());
        }
        return new SaResult(200,"注销成功",null);
    }

    @Override
    public boolean sendCode(LoginByCodeDTO loginByCodeDTO) {
        String phone = loginByCodeDTO.getPhone();
        if (!Validator.isValidPhoneNumber(phone)) {
            throw new BusinessException(ResponseCode.FAILED_USER_PHONE);
        }
//        else if(null == userDao.getUserInfoByPhone(phone)){
//            throw new BusinessException(ResponseCode.FAILED_UNREGISTERED);
//        }
        String phoneCodeKey = getPhoneCodeKey(phone);
        Long expire = redisService.getExpire(phoneCodeKey, TimeUnit.SECONDS);
        if (expire != null && (phoneCodeExpiration * 60 - expire) < 60 ){
            throw new BusinessException(ResponseCode.FAILED_FREQUENT);
        }
        String codeTimeKey = getCodeTimeKey(phone);
        Long sendTimes = redisService.getAtomicLong(codeTimeKey);
        if (sendTimes != 0 && sendTimes >= sendLimit) {
            throw new BusinessException(ResponseCode.FAILED_TIME_LIMIT);
        }
        String code = isSend ? RandomUtil.randomNumbers(6) : Constants.DEFAULT_CODE;
        //存储到redis  数据结构：String  key：p:c:手机号  value :code
        redisService.setValue(phoneCodeKey, code, phoneCodeExpiration, TimeUnit.MINUTES);
        if (isSend) {
            boolean sendMobileCode = aliSmsService.sendMobileCode(phone, code);
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

    /**
     * 核对验证码
     * @param phone
     * @param code
     */
    private void checkCode(String phone, String code) {
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
    private String getPhoneCodeKey(String phone) {
        return Constants.PHONE_CODE_KEY + phone;
    }

    /**
     * 获取验证码有效时间的键
     * @param phone
     * @return 验证码有效时间的键
     */
    private String getCodeTimeKey(String phone) {
        return Constants.CODE_TIME_KEY + phone;
    }
}
