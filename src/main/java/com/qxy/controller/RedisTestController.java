package com.qxy.controller;

import com.qxy.common.response.Response;
import com.qxy.common.response.ResponseCode;
import com.qxy.infrastructure.redis.IRedisService;
import org.redisson.api.RMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/redis")
public class RedisTestController {

    @Resource
    private IRedisService redisService;

    @GetMapping("/test")
    public Response<String> testRedis() {
        try {
            // 测试基本操作
            redisService.setValue("test_key", "Redis connection test");
            String value = redisService.getValue("test_key");

            // 测试数值操作
            redisService.setAtomicLong("test_count", 0);
            long count = redisService.incr("test_count");

            // 测试Map结构
            RMap<String, String> map = redisService.getMap("test_map");
            map.fastPut("key1", "value1");

            return Response.<String>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data("Redis测试成功! value=" + value + ", count=" + count)
                    .build();

        } catch (Exception e) {
            return Response.<String>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info("Redis连接测试失败: " + e.getMessage())
                    .build();
        }
    }
}