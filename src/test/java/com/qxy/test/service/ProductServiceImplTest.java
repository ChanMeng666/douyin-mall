package com.qxy.test.service;


import com.aliyun.oss.OSS;
import com.qxy.common.exception.AppException;
import com.qxy.common.response.ResponseCode;
import com.qxy.controller.dto.product.CreateProductDTO;
import com.qxy.controller.dto.product.UpdateProductDTO;
import com.qxy.dao.ProductDao;
import com.qxy.model.po.ProductDO;
import com.qxy.model.res.ProductRes;
import com.qxy.service.PictureService;
import com.qxy.service.impl.PictureServiceImpl;
import com.qxy.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {


    @Mock
    private ProductDao productMapper;

    @Mock
    private PictureServiceImpl pictureService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_Success() {
        CreateProductDTO createProductDTO = new CreateProductDTO();
        createProductDTO.setName("Test Product");
        createProductDTO.setDescription("Test Description");
        createProductDTO.setStock(10);
        createProductDTO.setPrice(BigDecimal.valueOf(100.0));

        when(pictureService.upload(any(MultipartFile.class))).thenReturn("https://test-image-url.com");
        when(productMapper.insert(any(ProductDO.class))).thenReturn(1);

        productService.createProduct(createProductDTO , multipartFile);


        verify(pictureService, times(1)).upload(any(MultipartFile.class));
        verify(productMapper, times(1)).insert(any(ProductDO.class));
    }

    @Test
    void getProductById_Success() {
        ProductDO productDO = new ProductDO();
        productDO.setProductId(1);
        productDO.setName("Test Product");
        productDO.setDescription("Test Description");
        productDO.setStock(10);
        productDO.setPrice(BigDecimal.valueOf(100.0));
        productDO.setImageUrl("https://test-image-url.com");

        when(productMapper.selectById(1)).thenReturn(productDO);

        ProductRes productRes = productService.getProductById(1);

        assertNotNull(productRes);
        assertEquals(1, productRes.getProductId());
        assertEquals("Test Product", productRes.getName());
        assertEquals("Test Description", productRes.getDescription());
        assertEquals(10, productRes.getStock());
        assertEquals(100.0, productRes.getPrice());
        assertEquals("https://test-image-url.com", productRes.getImageUrl());
    }

    @Test
    void listProducts_Success() {
        ProductDO productDO = new ProductDO();
        productDO.setProductId(1);
        productDO.setName("Test Product");
        productDO.setDescription("Test Description");
        productDO.setStock(10);
        productDO.setPrice(BigDecimal.valueOf(100.0));
        productDO.setImageUrl("https://test-image-url.com");

        when(productMapper.selectAll()).thenReturn(Collections.singletonList(productDO));

        List<ProductRes> productResList = productService.listProducts();

        assertNotNull(productResList);
        assertEquals(1, productResList.size());
        assertEquals(1, productResList.get(0).getProductId());
        assertEquals("Test Product", productResList.get(0).getName());
        assertEquals("Test Description", productResList.get(0).getDescription());
        assertEquals(10, productResList.get(0).getStock());
        assertEquals(100.0, productResList.get(0).getPrice());
        assertEquals("https://test-image-url.com", productResList.get(0).getImageUrl());
    }

    @Test
    void updateProduct_Success() {
        UpdateProductDTO updateProductDTO = new UpdateProductDTO();
        updateProductDTO.setProductId(1);
        updateProductDTO.setName("Updated Product");
        updateProductDTO.setDescription("Updated Description");
        updateProductDTO.setStock(20);
        updateProductDTO.setPrice(BigDecimal.valueOf(200.0));

        when(productMapper.updateProduct(any(ProductDO.class))).thenReturn(1);

        productService.updateProduct(updateProductDTO);

        verify(productMapper, times(1)).updateProduct(any(ProductDO.class));
    }

    @Test
    void deleteProduct_Success() {
        when(productMapper.deleteProduct(1)).thenReturn(1);

        productService.deleteProduct(1);

        verify(productMapper, times(1)).deleteProduct(1);
    }
}
