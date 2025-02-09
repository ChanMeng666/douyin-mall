package com.qxy.test.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
@Profile("test")
@EnableAutoConfiguration(exclude = {
    RedisAutoConfiguration.class,
    TaskSchedulingAutoConfiguration.class,
    TaskExecutionAutoConfiguration.class,
    QuartzAutoConfiguration.class
})
@ConditionalOnProperty(
    name = {
        "spring.task.scheduling.enabled",
        "spring.scheduling.enabled",
        "spring.task.execution.enabled"
    },
    havingValue = "false",
    matchIfMissing = true
)
public class TestConfig {
    // 可以添加其他测试相关的配置
} 