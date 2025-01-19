package com.qxy.model.po;

import lombok.Data;

/**
 * @Author: dawang
 * @Description: 地址
 * @Date: 2025/1/18 22:46
 * @Version: 1.0
 */
@Data
public class Address {

    private String streetAddress;  // 街道地址
    private String city;           // 城市
    private String state;          // 州/省
    private String country;        // 国家
    private String zipCode;        // 邮政编码

    // getters and setters
}