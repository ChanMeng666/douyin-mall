package com.qxy.dao.mapper;

import com.qxy.dao.dataobject.ProductDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductMapper {

    @Insert("insert into product (name,description,price,image_url,stock) " +
            " values (#{name},#{description},#{price},#{imageUrl},#{stock})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "product_id")
    void insert(ProductDO productDO);

    @Select("select * from product where product_id = #{productId}")
    ProductDO selectById(Integer productId);
}
