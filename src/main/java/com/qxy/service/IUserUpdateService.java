package com.qxy.service;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 修改用户信息服务接口
 */
public interface IUserUpdateService {

    /**
     * 修改用户名
     * @param NewName
     * @return 布尔
     */
    Boolean UpdateUserName(String NewName);

    /**
     * 修改邮箱
     * @param NewEmail
     * @return 布尔
     */
    Boolean UpdateEmail(String NewEmail);

    /**
     * 修改手机号
     * @param NewPhone
     * @return 布尔
     */
    Boolean UpdatePhone(String NewPhone);

    /**
     * 修改密码
     * @param NewPassword
     * @return 布尔
     */
    Boolean UpdatePassword(String NewPassword, String OldPassword);

}
