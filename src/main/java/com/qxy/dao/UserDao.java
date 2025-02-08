package com.qxy.dao;

import com.qxy.model.po.User;
import org.apache.ibatis.annotations.Mapper;
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
     * 通过用户ID获取用户所有信息
     * @param userId
     * @return User
     */
    User getUserInfoByUserId(Integer userId);

    /**
     * 通过用户名获取用户所有信息
     * @param userName
     * @return User
     */
    User getUserInfoByUserName(String userName);

    String getPassWordByUserName(String userName);
}
