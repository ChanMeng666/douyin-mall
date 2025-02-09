package com.qxy.test.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

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
@EnableTransactionManagement
public class TestConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler() {
            @Override
            public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
                return null;
            }
            
            @Override
            public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
                return null;
            }
            
            @Override
            public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
                return null;
            }
            
            @Override
            public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
                return null;
            }
        };
    }
}