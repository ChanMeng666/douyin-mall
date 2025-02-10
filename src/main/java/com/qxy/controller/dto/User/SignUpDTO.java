package com.qxy.controller.dto.User;

import lombok.Data;

/**
 * @author Gloss66
 * @version 1.0
 * @description: TODO
 */

@Data
public class SignUpDTO {
    /** 用户名*/
    private String userName;
    /** 密码*/
    private String password;
    /** 邮箱*/
    private String email;
    /** 手机号*/
    private String phone;
}
