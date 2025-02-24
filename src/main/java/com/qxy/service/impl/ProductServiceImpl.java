package com.qxy.service.impl;


import com.qxy.common.constant.Constants;
import com.qxy.common.exception.AppException;
import com.qxy.common.response.ResponseCode;
import com.qxy.controller.dto.product.CreateProductDTO;
import com.qxy.controller.dto.product.UpdateProductDTO;
import com.qxy.model.po.ProductDO;
import com.qxy.dao.ProductDao;
import com.qxy.infrastructure.redis.RedissonService;
import com.qxy.service.PictureService;
import com.qxy.service.ProductService;
import com.qxy.model.res.ProductRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    public Integer createProduct(CreateProductDTO param, MultipartFile multipartFile) {
        // 上传图片到OSS并获取CDN的URL
        String imageUrl = pictureService.upload(multipartFile);

        // 存表
        ProductDO productDO = new ProductDO();
        productDO.setName(param.getName());
        productDO.setDescription(param.getDescription());
        productDO.setStock(param.getStock());
        productDO.setPrice(param.getPrice());
        productDO.setImageUrl(imageUrl);
        int row = productMapper.insert(productDO);
        if (row != 1) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
        }
        return productDO.getProductId();
    }

    @Override
    public ProductRes getProductById(Integer productId) {
        // 先查询缓存
        String cacheKey = Constants.RedisKey.PRODUCT_INFO_KEY + Constants.UNDERLINE + productId;
        ProductRes productRes = redissonService.getValue(cacheKey);
        if (productRes != null) {
            return productRes;
        }

        // 缓存中没有，查询数据库
        ProductDO productDO = productMapper.selectById(productId);
        if (productDO == null) {
            // 缓存空对象，防止缓存穿透
            redissonService.setValue(cacheKey, new ProductRes(),
                    Constants.RedisKey.PRODUCT_INFO_NULL_EXPIRE_TIME, TimeUnit.SECONDS);
            return null;
        }

        // 将查询结果存入缓存
        productRes = convertToRES(productDO);
        redissonService.setValue(cacheKey, productRes,
                Constants.RedisKey.PRODUCT_INFO_EXPIRE_TIME, TimeUnit.SECONDS);

        return productRes;
    }

    @Override
    public List<ProductRes> listProducts() {
        // 先查询缓存
        String cacheKey = Constants.RedisKey.PRODUCT_LIST_KEY;
        List<ProductRes> productResList = redissonService.getValue(cacheKey);
        if (productResList != null) {
            return productResList;
        }

        // 缓存中没有，查询数据库
        List<ProductDO> productDOs = productMapper.selectAll();
        productResList = productDOs.stream()
                .map(this::convertToRES)
                .collect(Collectors.toList());

        // 将查询结果存入缓存
        redissonService.setValue(cacheKey, productResList,
                Constants.RedisKey.PRODUCT_LIST_EXPIRE_TIME, TimeUnit.SECONDS);

        return productResList;
    }

    @Override
    public List<ProductRes> batchSelect(List<Integer> productId) {
        List<ProductDO> productDOS = productMapper.selectByIds(productId);
        return productDOS.stream()
                .map(this::convertToRES)
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
    public void updateProduct(UpdateProductDTO param) {
        // 更新数据库
        ProductDO productDO = new ProductDO();
        productDO.setProductId(param.getProductId());
        productDO.setName(param.getName());
        productDO.setUpdatedAt(new Date());
        productDO.setStock(param.getStock());
        productDO.setPrice(param.getPrice());
        productDO.setDescription(param.getDescription());
        int row = productMapper.updateProduct(productDO);
        if (row != 1) {
            log.warn(ResponseCode.UN_ERROR.getInfo());
            throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
        }

        // 清除缓存
        String cacheKey = Constants.RedisKey.PRODUCT_INFO_KEY + Constants.UNDERLINE + param.getProductId();
        redissonService.remove(cacheKey);
    }

    @Override
    public void deleteProduct(Integer productId) {
        // 删除数据库记录
        int row = productMapper.deleteProduct(productId);
        if (row != 1) {
            log.warn(ResponseCode.UN_ERROR.getInfo());
            throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
        }

        // 清除缓存
        String cacheKey = Constants.RedisKey.PRODUCT_INFO_KEY + Constants.UNDERLINE + productId;
        redissonService.remove(cacheKey);
    }

    @Override
    public ProductRes selectByName(String name) {
        // 先查询缓存
        String cacheKey = Constants.RedisKey.PRODUCT_INFO_KEY + Constants.UNDERLINE + name;
        ProductRes productRes = redissonService.getValue(cacheKey);
        if (productRes != null) {
            return productRes;
        }

        // 缓存中没有，查询数据库
        ProductDO productDO = productMapper.selectByName(name);
        if (productDO == null) {
            // 缓存空对象，防止缓存穿透
            redissonService.setValue(cacheKey, new ProductRes(),
                    Constants.RedisKey.PRODUCT_INFO_NULL_EXPIRE_TIME, TimeUnit.SECONDS);
            return null;
        }

        // 将查询结果存入缓存
        productRes = convertToRES(productDO);
        redissonService.setValue(cacheKey, productRes,
                Constants.RedisKey.PRODUCT_INFO_EXPIRE_TIME, TimeUnit.SECONDS);

        return productRes;
    }

    private ProductRes convertToRES(ProductDO productDO) {
        if (null == productDO) {
            log.warn(ResponseCode.ILLEGAL_PARAMETER.getInfo());
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode()
                    ,ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        ProductRes res = new ProductRes();
        res.setProductId(productDO.getProductId());
        res.setName(productDO.getName());
        res.setDescription(productDO.getDescription());
        res.setPrice(productDO.getPrice());
        res.setStock(productDO.getStock());
        res.setImageUrl(productDO.getImageUrl());
        return res;
    }



}
