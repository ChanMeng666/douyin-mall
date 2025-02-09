package com.qxy.test.config;

import com.qxy.service.AiOrderService;
import com.qxy.service.QwenAIService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestAIConfiguration {
    
    @Bean
    @Primary
    public AiOrderService aiService() {
        return Mockito.mock(AiOrderService.class);
    }
    
    @Bean
    @Primary
    public QwenAIService qwenAIService() {
        return Mockito.mock(QwenAIService.class);
    }
} 