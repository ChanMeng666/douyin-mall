package com.qxy.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Gloss66
 * @version 1.0
 * @description: TODO
 */
@Mapper
@Repository
public interface PermissionDao {
    /**
     * 通过userId获取用户权限
     * @param userId
     * @return List<String>
     */
    List<String> getPermissionByUserId(Integer userId);

}
