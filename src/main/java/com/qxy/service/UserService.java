package com.qxy.service;

import cn.dev33.satoken.util.SaResult;
import com.qxy.controller.dto.User.LoginDTO;
import org.springframework.stereotype.Repository;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 用户服务类
 */
@Repository
public interface UserService {

   SaResult Login(LoginDTO logindto);
}
