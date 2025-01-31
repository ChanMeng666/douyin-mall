package com.qxy.service;

import com.qxy.controller.param.CreateProductParam;
import com.qxy.dao.dataobject.ProductDO;
import org.springframework.web.multipart.MultipartFile;
import com.qxy.service.dto.ProductDTO;
import java.util.List;

public interface ProductService {
    /**
     * 创建商品
     * @param param
     * @param multipartFile
     * @return
     */
    Integer createProduct(CreateProductParam param, MultipartFile multipartFile);

    /**
     * 根据id查找商品
     * @param productId
     * @return
     */
    ProductDTO getProductById(Integer productId);

    /**
     * 获取商品列表
     * @return
     */
    List<ProductDTO> listProducts();

    /**
     * 根据id批量查找
     * @param productId
     * @return
     */
    List<ProductDTO> batchSelect(List<Integer> productId);

    /**
     * 根据id存储商品库存缓存
     * @param productId
     * @return
     */
    void storeProductStock(Integer productId);

}