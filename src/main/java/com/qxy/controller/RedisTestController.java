package com.qxy.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.qxy.common.response.Response;
import com.qxy.common.response.ResponseCode;
import com.qxy.infrastructure.redis.IRedisService;
import org.redisson.api.RMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SaIgnore
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

            redisService.setValue("test_key2", "Redis connection test2", 300, TimeUnit.SECONDS);
            String value2 = redisService.getValue("test_key2");
            Long expireTime = redisService.getExpire("test_key2",TimeUnit.SECONDS);
            // 测试数值操作
            redisService.setAtomicLong("test_count", 0);
            long count = redisService.incr("test_count");

            // 测试Map结构
            RMap<String, String> map = redisService.getMap("test_map");
            map.fastPut("key1", "value1");

            String data = "Redis测试成功! value=" + value + ", count=" + count
                    +" value2="+ value + ", expireTime="+ expireTime
                    ;
            return Response.<String>success(ResponseCode.SUCCESS, data);

        } catch (Exception e) {
            return Response.<String>fail(ResponseCode.UN_ERROR.getCode()
                    ,"Redis连接测试失败: ", e.getMessage());
        }
    }

    @GetMapping("/getNumValue")
    public Response<String> getNumValue(@RequestParam String key){
        try {
            Object value = redisService.getAtomicLong(key);
            Long expireTime = redisService.getExpire(key,TimeUnit.SECONDS);
            String data = "Redis测试成功! key="+key+", value=" + value.toString() + ", ValueType="
                    + value.getClass().getName() +", expireTime="+ expireTime
                    ;
            return Response.<String>success(ResponseCode.SUCCESS, data);
        } catch (Exception e) {
            if(!redisService.isExists(key))
                return Response.<String> fail(ResponseCode.UN_ERROR.getCode()
                        ,"不存在此键值对", e.getMessage());
            return Response.<String>fail(ResponseCode.UN_ERROR.getCode()
                    ,"Redis连接测试失败: ", e.getMessage());
        }
    }

    @GetMapping("/InsertNum")
    public Response<String> InsertNum(@RequestParam String key,
                                   @RequestParam Long value,
                                   @RequestParam Integer expireTime)
    {
        try {
            redisService.setAtomicLong(key,value);
            Object CatheValue = redisService.getAtomicLong(key);
//            redisService.setValue(key,value);
            redisService.setExpire(key,expireTime,TimeUnit.SECONDS);
//            Object CatheValue = redisService.getValue(key);
            Long CatheExpireTime = redisService.getExpire(key,TimeUnit.SECONDS);
//            String CatheValue = "";
//            Long CatheExpireTime = null;
            String data = "Redis测试成功! key="+key+", value=" + CatheValue.toString() + ", expireTime="+ CatheExpireTime
                    ;
            return Response.<String>success(ResponseCode.SUCCESS, data);
        } catch (Exception e) {
            return Response.<String>fail(ResponseCode.UN_ERROR.getCode()
                    ,"Redis插入键值对测试失败: ", e.getMessage());
        }
    }

    @GetMapping("/Incr")
    public Response<String> Incr(@RequestParam String key){
        try {
//            if(!redisService.isExists(key))
//                return Response.fail(ResponseCode.UN_ERROR.getCode(),"不存在此键值对");
            long CatheValue;
//            CatheValue = redisService.getValue(key);
//            redisService.setAtomicLong(key,CatheValue+1);
            CatheValue = redisService.incr(key);
//            redisService.setValue(key, CatheValue+1);
//            CatheValue = redisService.getValue(key);
            Long expireTime = redisService.getExpire(key,TimeUnit.SECONDS);
            String data = "Redis测试成功! key="+key+", value=" + CatheValue + ", expireTime="+ expireTime
                    ;
            return Response.<String>success(ResponseCode.SUCCESS, data);
        } catch (Exception e) {
            return Response.<String>fail(ResponseCode.UN_ERROR.getCode()
                    ,"Redis键值自增测试失败: ", e.getMessage());
        }
    }

    @GetMapping("/Del")
    public Response<String> Del(@RequestParam String key){
        try {
            if(!redisService.isExists(key))
                return Response.<String> fail(ResponseCode.UN_ERROR.getCode(),"不存在此键值对");
            String data = "Redis测试成功! key="+key+" 删除成功！"
                    ;
            redisService.remove(key);

            return Response.<String>success(ResponseCode.SUCCESS, data);
        } catch (Exception e) {
            return Response.<String>fail(ResponseCode.UN_ERROR.getCode()
                    ,"Redis键值删除测试失败: ", e.getMessage());
        }
    }
}