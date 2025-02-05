package com.qxy.dao.mapper;

import com.qxy.dao.dataobject.ProductDO;
import com.qxy.model.po.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultMap;
import java.util.List;

@Mapper
public interface ProductDao {
    @Insert("insert into products (name, description, price, stock, image_url) " +
            "values (#{name}, #{description}, #{price}, #{stock}, #{imageUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "product_id")
    void insert(ProductDO productDO);

    ProductDO selectById(Integer productId);

    List<ProductDO> selectAll();

    List<ProductDO> selectByIds(List<Integer> productId);

    void reduceProductStock(int productId, int quantity);

    List<Product> queryProducts();
}