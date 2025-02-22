package com.qxy.controller.dto.User;

import lombok.Getter;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 发送手机验证码DTO
 */
@Getter
public class SendSMSCodeDTO {
    /** 手机号*/
    private String phone;
}
