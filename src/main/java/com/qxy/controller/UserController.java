package com.qxy.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qxy.common.tool.Validator;
import com.qxy.controller.dto.User.*;
import com.qxy.model.po.User;
import com.qxy.service.ICodeService;
import com.qxy.service.IUserService;
import com.qxy.service.impl.StpServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    private ICodeService codeService;

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

    /**
     * 验证码登录
     * @param loginByCodedto
     * @return 响应结果
     */
    @PostMapping(value ="LoginByCode")
    public SaResult LoginByCode(@RequestBody LoginByCodeDTO loginByCodedto){
        return userService.LoginBySMSCode(loginByCodedto);
    }

    /**
     * 用户登出
     * @return 响应结果
     */
    @PostMapping(value ="doLogout",produces = {"application/json;charset=UTF-8"})
    public SaResult doLogout(){
        return userService.Logout();
    }

    /**
     * 用户注册
     * @param signupdto
     * @return 响应结果
     */
    @SaIgnore
    @PostMapping(value ="SignUp",produces = {"application/json;charset=UTF-8"})
    public SaResult SignUp(@RequestBody SignUpDTO signupdto){
        return userService.SignUp(signupdto);
    }

    /**
     * 用户注销
     * @return 响应结果
     */
    @DeleteMapping(value ="SignOut",produces = {"application/json;charset=UTF-8"})
    public SaResult SignOut(){
        return userService.SignOut();
    }

    /**
     * 获取会话登录状态
     * @return 响应结果
     */
    @GetMapping(value ="isLogin",produces = {"application/json;charset=UTF-8"})
    public SaResult isLogin() {
        return SaResult.data("当前会话是否登录：" + StpUtil.isLogin());
    }

    /**
     * 通过当前会话的登录账号获取用户账号信息
     * @return 响应结果
     */
    @GetMapping(value = "getInfo",produces = {"application/json;charset=UTF-8"})
    public SaResult getInfo(){
        User user = userService.getInfoByLoginId(StpUtil.getLoginId().toString());
        Map<String,String > map = new HashMap<>();
        map.put("phone",user.getPhone());
        map.put("userName",user.getUserName());
        map.put("email",user.getEmail());
        return SaResult.data(map);
    }

    /**
     * 获取当前会话的登录账号
     * @return 响应结果
     */
    @GetMapping(value = "getLoginId",produces = {"application/json;charset=UTF-8"})
    public SaResult getLoginId(){
        return SaResult.ok(StpUtil.getLoginId().toString());
    }

    /**
     * 获取用户权限信息
     * @return 响应结果
     */
    @GetMapping(value = "getPermission",produces = {"application/json;charset=UTF-8"})
    public SaResult getPermission(){
        return SaResult.ok(StpUtil.getPermissionList().toString());
    }

    /**
     * 获取用户身份信息
     * @return 响应结果
     */
    @GetMapping(value = "getRole",produces = {"application/json;charset=UTF-8"})
    public SaResult getRole(){
        return SaResult.ok(StpUtil.getRoleList().toString());
    }

    /**
     * 用户获取手机验证码
     * @param sendSMSCodedto
     * @return 响应结果
     */
    @PutMapping(value = "SendPhoneCode",produces = {"application/json;charset=UTF-8"})
    public SaResult SendPhoneCode(@RequestBody SendSMSCodeDTO sendSMSCodedto){
        if(userService.sendPhoneCode(sendSMSCodedto)) return SaResult.ok("验证码发送成功");
        return SaResult.error("发生错误");
    }

    /**
     * 用户获取邮箱验证码
     * @param sendEmailCodedto
     * @return 响应结果
     */
    @PutMapping(value = "SendEmailCode",produces = {"application/json;charset=UTF-8"})
    public SaResult SendEmailCode(@RequestBody SendEmailCodeDTO sendEmailCodedto){
        if(userService.SendEmailCode(sendEmailCodedto)) return SaResult.ok("验证码发送成功");
        return SaResult.error("发生错误");
    }

    /**
     * 核对验证码接口
     * @param loginByCodedto
     * @return 响应结果
     */
    @GetMapping(value = "checkCode",produces = {"application/json;charset=UTF-8"})
    public SaResult checkCode(@RequestBody LoginByCodeDTO loginByCodedto){
        String str = Validator.getKindOfAccount(loginByCodedto.getAccount());
        if(str.equals("")) return SaResult.error("输入账号格式有误");
        codeService.checkCode(loginByCodedto.getAccount(), loginByCodedto.getCode());
        return SaResult.ok("验证码核对成功");
    }
}
