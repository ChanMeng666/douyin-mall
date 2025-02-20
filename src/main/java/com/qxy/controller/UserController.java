package com.qxy.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.qxy.controller.dto.User.LoginByCodeDTO;
import com.qxy.controller.dto.User.LoginDTO;
import com.qxy.controller.dto.User.SignUpDTO;
import com.qxy.service.IAliSmsService;
import com.qxy.service.IUserService;
import com.qxy.service.impl.StpServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gloss66
 * @version 1.0
 * @description: TODO
 */
@Slf4j
@RestController
@RequestMapping("/user/")
public class UserController {

    @Qualifier("UserService")
    @Autowired
    private IUserService userService;
    @Autowired
    private StpServiceImpl stpService;
    @Autowired
    private IAliSmsService aliSmsService;

    /**
     * 用户登录
     * @param logindto 用户登录DTO
     * @return
     */
    @PostMapping(value ="doLogin")
    public SaResult doLogin(@RequestBody LoginDTO logindto) {
        log.info("*******************");
        log.info(logindto.toString());
        log.info("*******************");
        return userService.Login(logindto);
    }
    @PostMapping(value ="LoginByCode")
    public SaResult LoginByCode(@RequestBody LoginByCodeDTO loginByCodedto){
        return userService.LoginBySMSCode(loginByCodedto);
    }

    @PostMapping(value ="doLogout",produces = {"application/json;charset=UTF-8"})
    public SaResult doLogout(){
        return userService.Logout();
    }

    @SaIgnore
    @PostMapping(value ="SignUp",produces = {"application/json;charset=UTF-8"})
    public SaResult SignUp(@RequestBody SignUpDTO signupdto){
        return userService.SignUp(signupdto);
    }

    @DeleteMapping(value ="SignOut",produces = {"application/json;charset=UTF-8"})
    public SaResult SignOut(){
        return userService.SignOut();
    }
//    @SaCheckLogin
    @GetMapping(value ="isLogin",produces = {"application/json;charset=UTF-8"})
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
    @GetMapping(value = "getLoginId",produces = {"application/json;charset=UTF-8"})
    public SaResult getLoginId(){
        return SaResult.ok(StpUtil.getLoginId().toString());
    }

    @GetMapping(value = "getPermission",produces = {"application/json;charset=UTF-8"})
    public SaResult getPermission(){
        return SaResult.ok(StpUtil.getPermissionList().toString());
    }

    @GetMapping(value = "getRole",produces = {"application/json;charset=UTF-8"})
    public SaResult getRole(){
        return SaResult.ok(StpUtil.getRoleList().toString());
    }

    @PutMapping(value = "SendCode",produces = {"application/json;charset=UTF-8"})
    public SaResult SendCode(@RequestBody LoginByCodeDTO loginByCodedto){
        if(userService.sendCode(loginByCodedto)) return SaResult.ok("验证码发送成功");
        return SaResult.error("发生错误");
    }
}
