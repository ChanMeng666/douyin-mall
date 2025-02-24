package com.qxy.controller.dto.User;

import lombok.Data;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 手机验证码登录DTO
 */
@Data
public class LoginByCodeDTO {
    /** 账号*/
    private String account;
    /** 验证码*/
    private String code;
    /** 登录设备*/
    private String loginDevice;
}
