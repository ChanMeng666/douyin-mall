package com.qxy.service;

import com.qxy.controller.dto.product.CreateProductDTO;
import com.qxy.controller.dto.product.UpdateProductDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.qxy.model.res.ProductRes;
import java.util.List;

public interface ProductService {
    /**
     * 创建商品
     * @param param
     * @param multipartFile
     * @return
     */
    @Transactional
    Integer createProduct(CreateProductDTO param, MultipartFile multipartFile);

    /**
     * 根据id查找商品
     * @param productId
     * @return
     */
    ProductRes getProductById(Integer productId);

    /**
     * 获取商品列表
     * @return
     */
    List<ProductRes> listProducts();

    /**
     * 根据id批量查找
     * @param productId
     * @return
     */
    List<ProductRes> batchSelect(List<Integer> productId);

    /**
     * 根据id存储商品库存缓存
     * @param productId
     * @return
     */
    void storeProductStock(Integer productId);

    /**
     * 更新商品
     * @param param
     */
    @Transactional
    void updateProduct(UpdateProductDTO param);

    /**
     * 删除商品
     * @param productId
     */
    @Transactional
    void deleteProduct(Integer productId);
}