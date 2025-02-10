package com.qxy.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.qxy.controller.dto.User.LoginDTO;
import com.qxy.controller.dto.User.SignUpDTO;
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
    public SaResult Login(LoginDTO logindto){
        if(!StpUtil.isLogin()) {
            String username = logindto.getUserName();
            String password = logindto.getPassword();
            if (userDao.getUserInfoByUserName(username) == null) return new SaResult(401,"用户名不存在",null);
            else if (userDao.getUserInfoByUserName(username).getPassword().equals(password)) {
                StpUtil.login(username);
                SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
                return new SaResult(200,"登录成功" + tokenInfo,null);
            } else return new SaResult(401,"密码错误，登录失败",null);
        }
        else return new SaResult(200,"已登录，请勿重复登录"+StpUtil.getTokenInfo(),null);
    }

    @Override
    public SaResult Logout(){
        if(StpUtil.isLogin()) return new SaResult(200,"成功退出登录",null);
        else return new SaResult(401,"您还未登录",null);
    }

    @Override
    public SaResult SignUp(SignUpDTO signupdto){
        String username = signupdto.getUserName();
        String email = signupdto.getEmail();
        String phone = signupdto.getPhone();
        if(username==null || email==null || phone==null) return new SaResult(200,"参数不能为空",null);
        //未注册
        if(userDao.getUserInfoByUserName(username)!=null)
            return new SaResult(200,"用户名已存在",username);
        else if (userDao.getUserInfoByEmail(email)!=null)
            return new SaResult(200,"邮箱已注册",email);
        else if (userDao.getUserInfoByPhone(phone)!=null)
            return new SaResult(200,"手机号已注册",phone);
        else {
            User user = new User();
            user.setUserName(signupdto.getUserName());
            user.setPassword(signupdto.getPassword());
            user.setEmail(signupdto.getEmail());
            user.setPhone(signupdto.getPhone());
            userDao.createUser(user,1);
            return new SaResult(200,"注册成功",user);
        }
    }

}
