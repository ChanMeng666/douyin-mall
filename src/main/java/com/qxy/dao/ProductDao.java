package com.qxy.dao;

import com.qxy.model.po.ProductDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductDao {

    int insert(ProductDO productDO);

    ProductDO selectById(Integer productId);

    List<ProductDO> selectAll();

    List<ProductDO> selectByIds(List<Integer> productId);

    void reduceProductStock(int productId, int quantity);

    int updateProduct(ProductDO productDO);

    int deleteProduct(Integer productId);
}