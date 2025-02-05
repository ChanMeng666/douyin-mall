package com.qxy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "redis.sdk.config")
public class RedisProperties {
    private String host;
    private int port;
    private int poolSize;
    private int minIdleSize;
    private int idleTimeout;
    private int connectTimeout;
    private int retryAttempts;
    private int retryInterval;
    private int pingInterval;
    private boolean keepAlive;
} 