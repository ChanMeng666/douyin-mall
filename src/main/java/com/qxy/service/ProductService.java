package com.qxy.service;

import com.qxy.controller.param.CreateProductParam;
import org.springframework.web.multipart.MultipartFile;
import com.qxy.service.dto.ProductDTO;

public interface ProductService {
    Long createProduct(CreateProductParam param, MultipartFile multipartFile);
    ProductDTO getProductById(Integer productId);
}
