package com.qxy.model.po;

import lombok.Data;

import java.util.Date;

/**
 * @Author: dawang
 * @Description: 用户实体
 * @Date: 2025/2/1 20:54
 * @Version: 1.0
 */
@Data
public class User {
    private Integer userId;
    private String userName;
    private String Password;
    private String email;
    private String phone;
    private Date createdAt;
    private Date updatedAt;
}
