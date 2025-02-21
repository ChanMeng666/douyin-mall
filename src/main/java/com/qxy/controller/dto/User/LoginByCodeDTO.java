package com.qxy.controller.dto.User;

import lombok.Data;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 手机验证码登录DTO
 */
@Data
public class LoginByCodeDTO {
//    /** 手机号*/
//    private String phone;
//    /** 邮箱*/
//    private String email;
    /** 账号*/
    private String account;
    /** 验证码*/
    private String code;
    /** 登录设备*/
    private String loginDevice;
}
