package com.qxy.service.impl;

import com.qxy.dao.UserDao;
import com.qxy.model.po.User;
import com.qxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Gloss66
 * @version 1.0
 * @description: TODO
 */
@Service
public class UserServiceImpl implements UserService {
//    private final UserDao userDao;
//
//    public UserServiceImpl(UserDao userDao) {
//        this.userDao = userDao;
//    }
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserInfoByUserId(Integer userId){
        return userDao.getUserInfoByUserId(userId);
    }
    @Override
    public User getUserInfoByUserName(String userName){
        return userDao.getUserInfoByUserName(userName);
    }

    public String getPassWordByUserName(String userName){
        return userDao.getPassWordByUserName(userName);
    }
}
