package com.qxy.dao;

import com.qxy.model.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Gloss66
 * @version 1.0
 * @description: TODO
 */

@Mapper
@Repository
public interface UserDao {
    /**
     * 通过用户ID获取User类的成员
     * @param userId
     * @return User
     */
    User getUserInfoByUserId(Integer userId);

    /**
     * 通过用户名获取User类的成员
     * @param userName
     * @return User
     */
    User getUserInfoByUserName(String userName);

    /**
     * 通过邮箱获取User类的成员
     * @param email
     * @return User
     */
    User getUserInfoByEmail(String email);

    /**
     * 通过手机号获取User类的成员
     * @param phone
     * @return User
     */
    User getUserInfoByPhone(String phone);

    /**
     * 通过用户名获取用户密码
     * @param userName
     * @return
     */
    String getPassWordByUserName(String userName);

    /**
     * 创建用户
     * @param user
     */
    void createUser(User user, @Param("role_id") Integer role_id);
}
