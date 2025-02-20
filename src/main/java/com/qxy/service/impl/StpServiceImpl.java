package com.qxy.service.impl;

/**
 * @author Gloss66
 * @version 1.0
 * @description: Satoken权限服务类
 */

import cn.dev33.satoken.stp.StpInterface;
import com.qxy.dao.PermissionDao;
import com.qxy.dao.RoleDao;
import com.qxy.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpServiceImpl implements StpInterface {

    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDao userDao;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Integer useId = userDao.getUserInfoByLoginId(loginId.toString()).getUserId();
        return permissionDao.getPermissionByUserId(useId);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Integer useId = userDao.getUserInfoByLoginId(loginId.toString()).getUserId();
        return roleDao.getRoleByUserId(useId);
    }

}

