package com.qxy.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductDO implements Serializable {
    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
    /**
     * 商品名
     */
    private String name;

    /**
     * 图片索引
     */
    private String imageUrl;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 描述
     */
    private String description;

    /**
     * 存货量
     */
    private Integer stock;

    /**
     * 商品状态
     */
    private String status;
}
