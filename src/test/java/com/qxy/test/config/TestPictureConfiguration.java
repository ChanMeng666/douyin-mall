package com.qxy.test.config;

import com.qxy.service.PictureService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestPictureConfiguration {
    
    @Bean
    @Primary
    public PictureService pictureService() {
        return Mockito.mock(PictureService.class);
    }
} 