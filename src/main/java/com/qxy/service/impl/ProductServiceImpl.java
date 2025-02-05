package com.qxy.service.impl;


import com.qxy.common.constant.Constants;
import com.qxy.common.exception.AppException;
import com.qxy.common.response.ResponseCode;
import com.qxy.controller.param.CreateProductParam;
import com.qxy.controller.param.UpdateProductParam;
import com.qxy.dao.dataobject.ProductDO;
import com.qxy.dao.ProductDao;
import com.qxy.infrastructure.redis.RedissonService;
import com.qxy.service.PictureService;
import com.qxy.service.ProductService;
import com.qxy.service.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
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
        int row = productMapper.insert(productDO);
        if(row != 1) {
            log.warn(ResponseCode.UN_ERROR.getInfo());
            throw new AppException(ResponseCode.UN_ERROR.getCode(),ResponseCode.UN_ERROR.getInfo());
        }
        return productDO.getProductId();
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

    @Override
    public void updateProduct(UpdateProductParam param) {
        //构造更新对象
        ProductDO productDO = new ProductDO();
        productDO.setProductId(param.getProductId());
        productDO.setName(param.getName());
        productDO.setUpdatedAt(new Date());
        productDO.setStock(param.getStock());
        productDO.setPrice(param.getPrice());
        productDO.setDescription(param.getDescription());
        //调用Dao
        int row = productMapper.updateProduct(productDO);
        if(row != 1) {
            log.warn(ResponseCode.UN_ERROR.getInfo());
            throw new AppException(ResponseCode.UN_ERROR.getCode(),ResponseCode.UN_ERROR.getInfo());
        }
    }

    @Override
    public void deleteProduct(Integer productId) {
        int row = productMapper.deleteProduct(productId);
        if(row != 1) {
            log.warn(ResponseCode.UN_ERROR.getInfo());
            throw new AppException(ResponseCode.UN_ERROR.getCode(),ResponseCode.UN_ERROR.getInfo());
        }
    }

    private ProductDTO convertToDTO(ProductDO productDO) {
        if (null == productDO) {
            log.warn(ResponseCode.ILLEGAL_PARAMETER.getInfo());
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode()
                    ,ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        ProductDTO dto = new ProductDTO();
        dto.setProductId(productDO.getProductId());
        dto.setName(productDO.getName());
        dto.setDescription(productDO.getDescription());
        dto.setPrice(productDO.getPrice());
        dto.setStock(productDO.getStock());
        dto.setImageUrl(productDO.getImageUrl());
        return dto;
    }



}
