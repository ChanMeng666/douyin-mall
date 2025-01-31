package com.qxy.service.impl;


import com.qxy.common.constant.Constants;
import com.qxy.controller.param.CreateProductParam;
import com.qxy.dao.dataobject.ProductDO;
import com.qxy.dao.mapper.ProductDao;
import com.qxy.infrastructure.redis.RedissonService;
import com.qxy.service.PictureService;
import com.qxy.service.ProductService;
import com.qxy.service.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productMapper;
    @Autowired
    PictureService pictureService;
    @Resource
    private RedissonService redissonService;

    @Override
    public Integer createProduct(CreateProductParam param, MultipartFile multipartFile) {
        String fileName = pictureService.savePicture(multipartFile);
        //存表
        ProductDO productDO = new ProductDO();
        productDO.setName(param.getName());
        productDO.setDescription(param.getDescription());
        productDO.setStock(param.getStock());
        productDO.setPrice(param.getPrice());
        productDO.setImageUrl(fileName);
        productMapper.insert(productDO);
        return productDO.getId();
    }

    @Override
    public ProductDTO getProductById(Integer productId) {
        ProductDO productDO = productMapper.selectById(productId);
        if (productDO == null) {
            return null;
        }
        return convertToDTO(productDO);
    }

    @Override
    public List<ProductDTO> listProducts() {
        List<ProductDO> productDOs = productMapper.selectAll();
        return productDOs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> batchSelect(List<Integer> productId) {
        List<ProductDO> productDOS = productMapper.selectByIds(productId);
        return productDOS.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void storeProductStock(Integer productId) {
        //先查询缓存
        String cacheKey = Constants.RedisKey.PRODUCT_COUNT_KEY + Constants.UNDERLINE + productId;
        Long stockLong =redissonService.getAtomicLong(cacheKey);
        if (stockLong != null) {
            return ;
        }
        //查询数据库
        ProductDO productDO = productMapper.selectById(productId);
        redissonService.setAtomicLong(cacheKey, productDO.getStock());

    }

    private ProductDTO convertToDTO(ProductDO productDO) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(productDO.getId());
        dto.setName(productDO.getName());
        dto.setDescription(productDO.getDescription());
        dto.setPrice(productDO.getPrice());
        dto.setStock(productDO.getStock());
        dto.setImageUrl(productDO.getImageUrl());
        return dto;
    }



}
