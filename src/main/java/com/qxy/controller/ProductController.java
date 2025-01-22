package com.qxy.controller;


import com.qxy.common.response.Response;
import com.qxy.common.response.ResponseCode;
import com.qxy.controller.param.CreateProductParam;
import com.qxy.service.PictureService;
import com.qxy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    PictureService pictureService;

    @Autowired
    ProductService productService;

    @RequestMapping("/pic/upload")
    public String uploadPic (MultipartFile multipartFile) {
        return pictureService.savePicture(multipartFile);
    }

    @RequestMapping("/product/create")
    public Response<Long> createProduct (@RequestPart("param") CreateProductParam param,
                                         @RequestPart("productPic") MultipartFile multipartFile) {
        return Response.<Long>builder().code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(productService.createProduct(param,multipartFile)).build();
    }
}
