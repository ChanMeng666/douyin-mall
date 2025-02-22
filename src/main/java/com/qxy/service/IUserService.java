package com.qxy.service;

import cn.dev33.satoken.util.SaResult;
import com.qxy.controller.dto.User.*;
import com.qxy.model.po.User;
import org.springframework.stereotype.Repository;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 用户服务类
 */
@Repository
public interface IUserService {

   /**
    * 用户账号登录
    * @param logindto
    * @return SaResult类
    */
   SaResult Login(LoginDTO logindto);

   /**
    * 手机验证码登录
    * @param loginByCodedto
    * @return SaResult
    */
   SaResult LoginBySMSCode(LoginByCodeDTO loginByCodedto);

   /**
    * 退出登录
    * @return SaResult
    */
   SaResult Logout();

   /**
    * 用户注册
    * @return SaResult
    */
   SaResult SignUp(SignUpDTO signupdto);

   /**
    * 用户注销
    * @return SaResult
    */
   public SaResult SignOut();

   /**
    * 获取手机短信验证码
    * @param sendSMSCodedto
    */
   boolean sendPhoneCode(SendSMSCodeDTO sendSMSCodedto);

   /**
    * 获取邮箱验证码
    * @param sendEmailCodedto
    * @return
    */
   public boolean SendEmailCode(SendEmailCodeDTO sendEmailCodedto);

   /**
    * 通过当前会话的登录账号获取用户信息
    * @param loginId
    */
   User getInfoByLoginId(String loginId);
}
