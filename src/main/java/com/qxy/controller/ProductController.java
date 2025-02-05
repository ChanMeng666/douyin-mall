package com.qxy.controller;


import com.qxy.common.exception.AppException;
import com.qxy.common.response.Response;
import com.qxy.common.response.ResponseCode;
import com.qxy.controller.param.CreateProductParam;
import com.qxy.controller.param.UpdateProductParam;
import com.qxy.controller.result.FindProductListResult;
import com.qxy.service.PictureService;
import com.qxy.service.ProductService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.qxy.service.dto.ProductDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    PictureService pictureService;

    @Autowired
    ProductService productService;

    /**
     * 上传图片
     * @param multipartFile
     * @return
     */
    @RequestMapping("/pic/upload")
    public String uploadPic (MultipartFile multipartFile) {
        return pictureService.savePicture(multipartFile);
    }

    /**
     * 创建商品
     * @param param 请求参数
     * @param multipartFile 文件传输
     * @return
     */
    @RequestMapping("/create")
    public Response<Integer> createProduct (@RequestPart("param") CreateProductParam param,
                                         @RequestPart("productPic") MultipartFile multipartFile) {
        return Response.<Integer>builder().code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(productService.createProduct(param,multipartFile)).build();
    }

    /**
     * 根据id查询商品信息
     * @param productId 商品id
     * @return
     */
    @RequestMapping("/get")
    public Response<List<FindProductListResult>> getProduct(@RequestParam List<Integer> productId){
        if(null == productId||productId.size() == 0) {
            log.warn("请求参数为空");
            return Response.success();
        }
        List<ProductDTO> products = productService.batchSelect(productId);
        return Response.<List<FindProductListResult>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(convertToFindProductListResult(products))
                .build();
    }

    /**
     * 展示商品列表
     * @return
     */
    @GetMapping("/list")
    public Response<List<FindProductListResult>> listProducts() {
        List<ProductDTO> products = productService.listProducts();
        return Response.<List<FindProductListResult>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(convertToFindProductListResult(products))
                .build();
    }

    @RequestMapping("/update")
    public Response<Boolean> updateProduct (@NonNull UpdateProductParam param) {
        if (null == param.getProductId() || param.getProductId() <= 0) {
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(Boolean.FALSE)
                    .build();
        }
        productService.updateProduct(param);
        return Response.<Boolean>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(Boolean.TRUE)
                .build();
    }

    @RequestMapping("/delete/{productId}")
    public Response<Boolean> deleteProduct (@PathVariable Integer productId) {
        if(productId <= 0) {
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(Boolean.FALSE)
                    .build();
        }
        productService.deleteProduct(productId);
        return Response.<Boolean>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(Boolean.TRUE)
                .build();
    }

    private List<FindProductListResult> convertToFindProductListResult(List<ProductDTO> products) {
        if (null == products) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode()
                    ,ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        return products.stream()
                .map(productDTO -> {
                    FindProductListResult result = new FindProductListResult();
                    result.setDescription(productDTO.getDescription());
                    result.setProductId(productDTO.getProductId());
                    result.setName(productDTO.getName());
                    result.setStock(productDTO.getStock());
                    result.setPrice(productDTO.getPrice());
                    result.setImageUrl(productDTO.getImageUrl());
                    return result;
                }).collect(Collectors.toList());
    }
}
