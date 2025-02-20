package com.qxy.controller.dto.User;

import lombok.Data;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 用户登录DTO
 */

@Data
public class LoginDTO {
//    /** 用户名*/
//    private String userName;
//    /** 邮箱*/
//    private String email;
    /** 登录账号*/
    private String loginId;
    /** 密码*/
    private String password;

}
