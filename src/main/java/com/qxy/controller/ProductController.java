package com.qxy.controller;


import com.qxy.common.exception.AppException;
import com.qxy.common.response.Response;
import com.qxy.common.response.ResponseCode;
import com.qxy.controller.dto.product.CreateProductDTO;
import com.qxy.controller.dto.product.UpdateProductDTO;
import com.qxy.controller.dto.product.FindProductListDTO;
import com.qxy.service.PictureService;
import com.qxy.service.ProductService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.qxy.model.res.ProductRes;
import org.springframework.web.bind.annotation.*;

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
        return pictureService.upload(multipartFile);
    }

    /**
     * 创建商品
     * @param param 请求参数
     * @param multipartFile 文件传输
     * @return
     */
    @RequestMapping("/create")
    public Response<Integer> createProduct (@RequestPart("param") CreateProductDTO param,
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
    public Response<List<FindProductListDTO>> getProduct(@RequestParam List<Integer> productId){
        if(null == productId||productId.size() == 0) {
            log.warn("请求参数为空");
            return Response.success();
        }
        List<ProductRes> products = productService.batchSelect(productId);
        return Response.<List<FindProductListDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(convertToFindProductListResult(products))
                .build();
    }

    /**
     * 根据商品名查询商品
     * @param name
     * @return
     */
    @RequestMapping("/getByName")
    public Response<FindProductListDTO> getProductByName(String name) {
        if(null == name || name.length() == 0) {
            log.warn("请求参数为空");
            return Response.success();
        }
        ProductRes product = productService.selectByName(name);
        FindProductListDTO productDTO = new FindProductListDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setName(product.getName());
        productDTO.setStock(product.getStock());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageUrl(product.getImageUrl());
        return Response.<FindProductListDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(productDTO)
                .build();
    }

    /**
     * 展示商品列表
     * @return
     */
    @GetMapping("/list")
    public Response<List<FindProductListDTO>> listProducts() {
        List<ProductRes> products = productService.listProducts();
        return Response.<List<FindProductListDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(convertToFindProductListResult(products))
                .build();
    }

    @RequestMapping("/update")
    public Response<Boolean> updateProduct (@NonNull UpdateProductDTO param) {
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

    private List<FindProductListDTO> convertToFindProductListResult(List<ProductRes> products) {
        if (null == products) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode()
                    ,ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        return products.stream()
                .map(productDTO -> {
                    FindProductListDTO result = new FindProductListDTO();
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
