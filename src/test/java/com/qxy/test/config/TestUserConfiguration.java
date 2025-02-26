package com.qxy.test.config;

import com.qxy.service.IUserService;
import com.qxy.service.ProductService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;


@TestConfiguration
@Profile("test")
public class TestUserConfiguration {
    @Bean
    @Primary
    public IUserService userService() {
        return Mockito.mock(IUserService.class);
    }
}
