package com.qxy.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.qxy.common.exception.BusinessException;
import com.qxy.common.response.ResponseCode;
import com.qxy.common.tool.Validator;
import com.qxy.dao.UserDao;
import com.qxy.dao.UserUpdateDao;
import com.qxy.model.po.User;
import com.qxy.service.IUserUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 修改用户信息服务实现
 */
@Service("UserUpdateService")
public class UserUpdateServiceImpl implements IUserUpdateService {

    @Autowired
    private UserUpdateDao userUpdateDao;
    @Autowired
    private UserDao userDao;

    @Override
    public Boolean UpdateUserName(String NewName) {
        if(!Validator.isValidUsername(NewName))
            throw new BusinessException(ResponseCode.ILLEGAL_INPUT_FORMAT,"用户名格式错误");
        try{
            String loginid = StpUtil.getLoginId().toString();
            Integer userid = userDao.getUserInfoByLoginId(loginid).getUserId();
            userUpdateDao.UpdateUserNameById(NewName,userid);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean UpdateEmail(String NewEmail) {
        if(!Validator.isValidEmail(NewEmail))
            throw new BusinessException(ResponseCode.ILLEGAL_INPUT_FORMAT,"邮箱格式错误");
        try{
            String loginid = StpUtil.getLoginId().toString();
            Integer userid = userDao.getUserInfoByLoginId(loginid).getUserId();
            userUpdateDao.UpdateEmailById(NewEmail,userid);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean UpdatePhone(String NewPhone) {
        if(!Validator.isValidPhoneNumber(NewPhone))
            throw new BusinessException(ResponseCode.ILLEGAL_INPUT_FORMAT,"手机号格式错误");
        try{
            String loginid = StpUtil.getLoginId().toString();
            Integer userid = userDao.getUserInfoByLoginId(loginid).getUserId();
            userUpdateDao.UpdatePhoneById(NewPhone,userid);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean UpdatePassword(String NewPassword, String OldPassword) {
            if(NewPassword==null||OldPassword==null||NewPassword.equals("")||OldPassword.equals(""))
                throw new BusinessException(ResponseCode.FAILED_VOID_PARAMETER);
            if(!Validator.isValidPassword(NewPassword))
                throw new BusinessException(ResponseCode.ILLEGAL_INPUT_FORMAT,"新密码格式错误");
            String loginid = StpUtil.getLoginId().toString();
            User user = userDao.getUserInfoByLoginId(loginid);
            if(user.getPassword().equals(SaSecureUtil.sha256(NewPassword)))
                throw new BusinessException(ResponseCode.FAILED_PASSWORD_REUSE);
            if(!user.getPassword().equals(SaSecureUtil.sha256(OldPassword)))
                throw new BusinessException(ResponseCode.FAILED_ERROR_PASSWORD,"原密码错误");
            userUpdateDao.UpdatePasswordById(SaSecureUtil.sha256(NewPassword),user.getUserId());
            return true;
    }

}
