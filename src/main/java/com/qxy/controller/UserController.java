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
//        if(!StpUtil.isLogin()){
//            if(userService.getUserInfoByUserName(username)==null) return SaResult.error("用户名不存在");
//            else if(userService.getUserInfoByUserName(username).getPassword().equals(password)){
//                StpUtil.login(username);
//                SaTokenInfo tokenInfo= StpUtil.getTokenInfo();
//                return SaResult.ok("登录成功"+tokenInfo);
//            }
//            else return SaResult.error("密码错误，登录失败");
//        }
//        else return SaResult.ok("已登录，请勿重复登录"+StpUtil.getTokenInfo());
////        return SaResult.error("登录失败");
        return userService.Login(logindto);
    }

    @RequestMapping(value ="doLogout",produces = {"application/json;charset=UTF-8"})
    public SaResult doLogout(){
        StpUtil.logout();
        return SaResult.ok("用户退出登录");
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
