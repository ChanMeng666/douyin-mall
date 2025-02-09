package com.qxy.test.config;

import org.mockito.Mockito;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import com.qxy.infrastructure.redis.IRedisService;

@TestConfiguration
@Profile("test")
public class TestRedisConfiguration {
    
    @Bean
    @Primary
    public RedissonClient redissonClient() {
        return Mockito.mock(RedissonClient.class);
    }
    
    @Bean
    @Primary
    public IRedisService redisService() {
        return Mockito.mock(IRedisService.class);
    }
} 