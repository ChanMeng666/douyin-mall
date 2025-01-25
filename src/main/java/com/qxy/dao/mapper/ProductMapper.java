package com.qxy.dao.mapper;

import com.qxy.dao.dataobject.ProductDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ProductMapper {
    @Insert("insert into products (name,description,price,stock) " +
            " values (#{name},#{description},#{price},#{stock})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "product_id")
    void insert(ProductDO productDO);

    @Select("select * from products where product_id = #{productId}")
    ProductDO selectById(Integer productId);

    @Select("select * from products")
    List<ProductDO> selectAll();
}