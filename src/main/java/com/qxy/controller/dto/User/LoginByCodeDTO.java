package com.qxy.controller.dto.User;

import lombok.Data;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 手机验证码登录DTO
 */
@Data
public class LoginByCodeDTO {
    /** 手机号*/
    private String phone;
    /** 验证码*/
    private String code;
}
