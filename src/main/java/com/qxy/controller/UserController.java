package com.qxy.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qxy.common.constant.Constants;
import com.qxy.common.response.Response;
import com.qxy.common.response.ResponseCode;
import com.qxy.common.tool.Validator;
import com.qxy.controller.dto.User.*;
import com.qxy.model.po.User;
import com.qxy.service.ICodeService;
import com.qxy.service.IUserService;
import com.qxy.service.IUserUpdateService;
import com.qxy.service.impl.StpServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
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
    @Autowired
    private IUserUpdateService userUpdateService;

    /**
     * 用户登录
     * @param logindto 用户登录DTO
     * @return
     */
    @PostMapping(value ="doLogin")
    public Response<?> doLogin(@RequestBody LoginDTO logindto,
                               @RequestParam boolean isRemember,
                               @RequestParam boolean isSevenDays) {
        if(userService.Login(logindto,isRemember,isSevenDays))
          return Response.success(ResponseCode.LOGIN_SUCCESS,StpUtil.getTokenInfo());
        return Response.fail(ResponseCode.LOGIN_ERROR,null);
    }

    /**
     * 验证码登录
     * @param loginByCodedto
     * @return 响应结果
     */
    @PostMapping(value ="LoginByCode")
    public Response<?> LoginByCode(@RequestBody LoginByCodeDTO loginByCodedto,
                                   @RequestParam boolean isRemember,
                                   @RequestParam boolean isSevenDays){
        if(userService.LoginByCode(loginByCodedto,isRemember,isSevenDays))
            return Response.success(ResponseCode.LOGIN_SUCCESS,StpUtil.getTokenInfo());
        return Response.fail(ResponseCode.LOGIN_ERROR,null);
    }

    /**
     * 用户登出
     * @return 响应结果
     */
    @PostMapping(value ="doLogout",produces = {"application/json;charset=UTF-8"})
    public Response<?> doLogout(){
        if(userService.Logout())
            return Response.success();
        return Response.fail();
    }

    /**
     * 用户注册
     * @param signupdto
     * @return 响应结果
     */
    @SaIgnore
    @PostMapping(value ="USER/SignUp",produces = {"application/json;charset=UTF-8"})
    public Response<?> UserSignUp(@RequestBody SignUpDTO signupdto){
        if(userService.SignUp(signupdto, Constants.ROLE_USER))
            return Response.success();
        return Response.fail();
    }

    /**
     * 商家注册
     * @param signupdto
     * @return 响应结果
     */
    @PostMapping(value ="MERCHANT/SignUp",produces = {"application/json;charset=UTF-8"})
    public Response<?> MerchantSignUp(@RequestBody SignUpDTO signupdto){
        if(userService.SignUp(signupdto, Constants.ROLE_MERCHANT))
            return Response.success();
        return Response.fail();
    }

    /**
     * 用户注销
     * @return 响应结果
     */
    @DeleteMapping(value ="SignOut",produces = {"application/json;charset=UTF-8"})
    public Response<?> SignOut(){
        if(userService.SignOut())
            return Response.success();
        return Response.fail();
    }

    /**
     * 获取会话登录状态
     * @return 响应结果
     */
    @GetMapping(value ="isLogin",produces = {"application/json;charset=UTF-8"})
    public Response<?> isLogin() {
        return Response.success("200","当前会话是否登录: " + StpUtil.isLogin());
    }

    /**
     * 通过当前会话的登录账号获取用户账号信息
     * @return 响应结果
     */
    @GetMapping(value = "getInfo",produces = {"application/json;charset=UTF-8"})
    public Response<?> getInfo(){
        User user = userService.getInfoByLoginId(StpUtil.getLoginId().toString());
        Map<String,String > map = new HashMap<>();
        map.put("phone",user.getPhone());
        map.put("userName",user.getUserName());
        map.put("email",user.getEmail());
        return Response.success("200","获取成功",map);
    }

    /**
     * 获取当前会话的登录账号
     * @return 响应结果
     */
    @GetMapping(value = "getLoginId",produces = {"application/json;charset=UTF-8"})
    public Response<?> getLoginId(){
        if(StpUtil.getLoginId()==null) return Response.fail("404","LoginId为空");
        return Response.success("200","获取成功",StpUtil.getLoginId().toString());
    }

    /**
     * 获取用户权限信息
     * @return 响应结果
     */
    @GetMapping(value = "getPermission",produces = {"application/json;charset=UTF-8"})
    public Response<?> getPermission(){
        return Response.success("200","获取成功",StpUtil.getPermissionList().toString());
    }

    /**
     * 获取用户身份信息
     * @return 响应结果
     */
    @GetMapping(value = "getRole",produces = {"application/json;charset=UTF-8"})
    public Response<?> getRole(){
        return Response.success("200","获取成功",StpUtil.getRoleList().toString());
    }

    /**
     * 用户获取手机验证码
     * @param sendSMSCodedto
     * @return 响应结果
     */
    @PostMapping(value = "SendPhoneCode",produces = {"application/json;charset=UTF-8"})
    public Response<?> SendPhoneCode(@RequestBody SendSMSCodeDTO sendSMSCodedto){
        if(userService.sendPhoneCode(sendSMSCodedto)) return Response.success("200","验证码发送成功");
        return Response.fail("500","发生错误");
    }

    /**
     * 用户获取邮箱验证码
     * @param sendEmailCodedto
     * @return 响应结果
     */
    @PostMapping(value = "SendEmailCode",produces = {"application/json;charset=UTF-8"})
    public Response<?> SendEmailCode(@RequestBody SendEmailCodeDTO sendEmailCodedto){
        if(userService.SendEmailCode(sendEmailCodedto)) return Response.success("200","验证码发送成功");
        return Response.fail("500","发生错误");
    }

    /**
     * 核对验证码接口
     * @param loginByCodedto
     * @return 响应结果
     */
    @GetMapping(value = "checkCode",produces = {"application/json;charset=UTF-8"})
    public Response<?> checkCode(@RequestBody LoginByCodeDTO loginByCodedto){
        String str = Validator.getKindOfAccount(loginByCodedto.getAccount());
        if(str.equals("")) return Response.fail(ResponseCode.ILLEGAL_INPUT_FORMAT, null);
        codeService.checkCode(loginByCodedto.getAccount(), loginByCodedto.getCode());
        return Response.success("200","验证码核对成功");
    }

    /**
     * 获取用户令牌信息
     * @return 响应结果
     */
    @GetMapping(value = "getTokenInfo",produces = {"application/json;charset=UTF-8"})
    public Response<?> getTokenInfo(){
        return Response.success("200","令牌信息",StpUtil.getTokenInfo());
    }

    @PutMapping(value = "UpdateUserName",produces = {"application/json;charset=UTF-8"})
    public Response<?> UpdateUserName(@RequestBody Map<String,String> NewName){
        if(userUpdateService.UpdateUserName(NewName.get("NewName")))
            return Response.success("200","修改成功",StpUtil.getTokenInfo());
        return Response.fail("401","修改失败",null);
    }

    @PutMapping(value = "UpdateEmail",produces = {"application/json;charset=UTF-8"})
    public Response<?> UpdateEmail(@RequestBody Map<String,String> NewEmail){
        if(userUpdateService.UpdateEmail(NewEmail.get("NewEmail")))
            return Response.success("200","修改成功",StpUtil.getTokenInfo());
        return Response.fail("401","修改失败",null);
    }

    @PutMapping(value = "UpdatePhone",produces = {"application/json;charset=UTF-8"})
    public Response<?> UpdatePhone(@RequestBody Map<String,String> NewPhone){
        if(userUpdateService.UpdatePhone(NewPhone.get("NewPhone")))
            return Response.success("200","修改成功",StpUtil.getTokenInfo());
        return Response.fail("401","修改失败",null);
    }

    @PutMapping(value = "UpdatePassword",produces = {"application/json;charset=UTF-8"})
    public Response<?> UpdatePassword(@RequestBody Map<String,String> updatePassword){
        String NewPassword = updatePassword.get("NewPassword");
        String OldPassword = updatePassword.get("OldPassword");
        if(userUpdateService.UpdatePassword(NewPassword, OldPassword))
            return Response.success("200","修改成功",StpUtil.getTokenInfo());
        return Response.fail("401","修改失败","未知原因");
    }

}
