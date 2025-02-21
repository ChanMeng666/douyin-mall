package com.qxy.controller.dto.User;

import lombok.Getter;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 发送邮箱验证码DTO
 */
@Getter
public class SendEmailCodeDTO {
    /** 邮箱*/
    private String email;
}
