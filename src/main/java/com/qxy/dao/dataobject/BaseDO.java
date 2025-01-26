package com.qxy.dao.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class BaseDO implements Serializable {
    //主键
    private Integer id;
    //创建时间
    private Date createdAt;
    //修改时间
    private Date updatedAt;
}
