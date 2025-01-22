package com.qxy.service;

import com.qxy.controller.param.CreateProductParam;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    Long createProduct(CreateProductParam param, MultipartFile multipartFile);
}
