package com.qxy.controller.dto.User;

import lombok.Data;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 用户登录DTO
 */

@Data
public class LoginDTO {
    /** 登录账号*/
    private String account;
    /** 密码*/
    private String password;
    /** 登录设备*/
    private String loginDevice;

}
