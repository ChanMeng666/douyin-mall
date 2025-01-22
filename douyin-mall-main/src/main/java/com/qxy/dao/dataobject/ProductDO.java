package com.qxy.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductDO extends BaseDO{
    /**
     * 奖品名
     */
    private String productName;

    /**
     * 图片索引
     */
    private String imageUrl;

    /**
     * 价格
     */
    private BigDecimal productPrice;

    /**
     * 描述
     */
    private String productDescription;

    /**
     * 存货量
     */
    private Integer stock;
}
