package com.qxy.service;

import cn.dev33.satoken.util.SaResult;
import com.qxy.controller.dto.User.LoginDTO;
import com.qxy.controller.dto.User.SignUpDTO;
import org.springframework.stereotype.Repository;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 用户服务类
 */
@Repository
public interface UserService {

   /**
    * 用户登录
    * @param logindto
    * @return SaResult类
    */
   SaResult Login(LoginDTO logindto);

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
}
