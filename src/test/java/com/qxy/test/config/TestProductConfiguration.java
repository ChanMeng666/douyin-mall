package com.qxy.test.config;

import com.qxy.service.ProductService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestProductConfiguration {
    
    @Bean
    @Primary
    public ProductService productService() {
        return Mockito.mock(ProductService.class);
    }
} 