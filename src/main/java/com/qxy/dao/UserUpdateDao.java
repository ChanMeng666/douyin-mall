package com.qxy.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 修改用户DAO
 */

@Mapper
@Repository
public interface UserUpdateDao {

    /**
     * 修改用户名
     * @param NewName
     * @return 布尔
     */
    void UpdateUserNameById(@Param("NewName")String NewName,@Param("UserId")Integer UserId);

    /**
     * 修改邮箱
     * @param NewEmail
     * @return 布尔
     */
    void UpdateEmailById(@Param("NewEmail")String NewEmail,@Param("UserId")Integer UserId);

    /**
     * 修改手机号
     * @param NewPhone
     * @return 布尔
     */
    void UpdatePhoneById(@Param("NewPhone")String NewPhone,@Param("UserId")Integer UserId);

    /**
     * 修改密码
     * @param NewPassword
     * @return 布尔
     */
    void UpdatePasswordById(@Param("NewPassword")String NewPassword,@Param("UserId")Integer UserId);
}
