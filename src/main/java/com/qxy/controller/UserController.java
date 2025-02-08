package com.qxy.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.qxy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gloss66
 * @version 1.0
 * @description: TODO
 */
@Slf4j
@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value ="doLogin",produces = {"application/json;charset=UTF-8"})
    public String doLogin(String username, String password) {
        if(!StpUtil.isLogin()){
            String password_db = userService.getUserInfoByUserName(username).getPassword();
            if(password_db==null)  return "登录失败";
            else if(userService.getUserInfoByUserName(username).getPassword().equals(password)){
                StpUtil.login(username);
                SaTokenInfo tokenInfo= StpUtil.getTokenInfo();
                return "登录成功"+tokenInfo;
            }
        }
        else return "已登录，请勿重复登录"+StpUtil.getTokenInfo();
        return "登录失败";

    }

    @SaCheckLogin
    @RequestMapping(value ="isLogin",produces = {"application/json;charset=UTF-8"})
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    @SaCheckLogin
    @RequestMapping(value = "getUsername",produces = {"application/json;charset=UTF-8"})
    public String getUsername(Integer id){
        System.out.println(userService.getUserInfoByUserId(id));
        return "用户名："+ userService.getUserInfoByUserId(id).getPassword();
    }

    @SaCheckLogin
    @RequestMapping(value = "getPassWord",produces = {"application/json;charset=UTF-8"})
    public String getPassWord(String username){
        if(userService.getUserInfoByUserName(username) == null)
            log.info("userService为null");
        else
            log.info("userService不为null");
        System.out.println("userService:"+userService.getUserInfoByUserName(username));

        return "密码:"+ userService.getUserInfoByUserName(username).getPassword();
//        return "密码:";
    }
}
