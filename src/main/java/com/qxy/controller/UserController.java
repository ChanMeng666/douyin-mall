package com.qxy.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.qxy.controller.dto.User.LoginDTO;
import com.qxy.service.UserService;
import com.qxy.service.impl.StpServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Autowired
    private StpServiceImpl stpService;

    /**
     * 用户登录
     * @param logindto 用户登录DTO
     * @return
     */
    @RequestMapping(value ="doLogin")
    public SaResult doLogin(@RequestBody LoginDTO logindto) {
        log.info("*******************");
        log.info(logindto.toString());
        log.info("*******************");
        return userService.Login(logindto);
    }

    @RequestMapping(value ="doLogout",produces = {"application/json;charset=UTF-8"})
    public SaResult doLogout(){
        return userService.Logout();
    }

    @RequestMapping(value ="SignUp",produces = {"application/json;charset=UTF-8"})
    public SaResult SignUp(){
        return userService.Logout();
    }

//    @SaCheckLogin
    @RequestMapping(value ="isLogin",produces = {"application/json;charset=UTF-8"})
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

////    @SaCheckLogin
//    @RequestMapping(value = "getUsername",produces = {"application/json;charset=UTF-8"})
//    public String getUsername(Integer id){
//        System.out.println(userService.getUserInfoByUserId(id));
//        return "用户名："+ userService.getUserInfoByUserId(id).getPassword();
//    }
//
//    @SaCheckLogin
//    @RequestMapping(value = "getPassWord",produces = {"application/json;charset=UTF-8"})
//    public String getPassWord(String username){
//        if(userService.getUserInfoByUserName(username) == null)
//            log.info("userService为null");
//        else
//            log.info("userService不为null");
//        System.out.println("userService:"+userService.getUserInfoByUserName(username));
//
//        return "密码:"+ userService.getUserInfoByUserName(username).getPassword();
////        return "密码:";
//    }
//
    @RequestMapping(value = "getLoginId",produces = {"application/json;charset=UTF-8"})
    public SaResult getLoginId(){
        return SaResult.ok(StpUtil.getLoginId().toString());
    }

    @RequestMapping(value = "getPermission",produces = {"application/json;charset=UTF-8"})
    public SaResult getPermission(){
        return SaResult.ok(StpUtil.getPermissionList().toString());
    }

    @RequestMapping(value = "getRole",produces = {"application/json;charset=UTF-8"})
    public SaResult getRole(){
        return SaResult.ok(StpUtil.getRoleList().toString());
    }
}
