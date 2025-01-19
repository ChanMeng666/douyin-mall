package com.qxy.dao.mapper;

import com.qxy.dao.dataobject.ProductDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ProductMapper {

    @Insert("insert into product (product_name,product_description,product_price,image_url) " +
            " values (#{productName},#{productDescription},#{productPrice},#{imageUrl})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void insert(ProductDO productDO);
}
