package com.qxy.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.qxy.controller.dto.User.LoginDTO;
import com.qxy.dao.UserDao;
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
    public SaResult Login(LoginDTO logindto){
        if(!StpUtil.isLogin()) {
            String username = logindto.getUserName();
            String password = logindto.getPassword();
            if (userDao.getUserInfoByUserName(username) == null) return new SaResult().setMsg("用户名不存在");
            else if (userDao.getUserInfoByUserName(username).getPassword().equals(password)) {
                StpUtil.login(username);
                SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
                return new SaResult().setMsg("登录成功" + tokenInfo);
            } else return new SaResult().setMsg("密码错误，登录失败");
        }
        else return new SaResult().setMsg("已登录，请勿重复登录"+StpUtil.getTokenInfo());
    }
}
