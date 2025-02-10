package com.qxy.dao;

import com.qxy.model.po.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface  AiProductDao {
/**
 * @author water
 * @description: TODO
 * @date 2025/2/3 20:15
 * @version 1.0
 */

     List<Product> searchProductsByName(@Param("keyword") String keywordS);
     void decreaseStock(@Param("productId") Integer productId, @Param("quantity") Integer quantity);
}
