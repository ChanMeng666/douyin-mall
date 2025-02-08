package com.qxy.service;

import com.qxy.model.po.User;
import org.springframework.stereotype.Repository;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 用户服务类
 */
@Repository
public interface UserService {
   /**
    *  获取用户信息
    *  @param userId
    *  @return User
    */
   User getUserInfoByUserId(Integer userId);

   /**
    *
    * @param userName
    * @return
    */
   User getUserInfoByUserName(String userName);

   String getPassWordByUserName(String userName);
}
