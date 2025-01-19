package com.qxy.service.impl;


import com.qxy.controller.param.CreateProductParam;
import com.qxy.dao.dataobject.ProductDO;
import com.qxy.dao.mapper.ProductMapper;
import com.qxy.service.PictureService;
import com.qxy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    PictureService pictureService;

    @Override
    public Long createProduct(CreateProductParam param, MultipartFile multipartFile) {
        String fileName = pictureService.savePicture(multipartFile);
        //存表
        ProductDO productDO = new ProductDO();
        productDO.setProductName(param.getProductName());
        productDO.setProductPrice(param.getProductPrice());
        productDO.setDescription(param.getProductDescription());
        productDO.setImageUrl(fileName);
        productMapper.insert(productDO);
        return productDO.getId();
    }
}
