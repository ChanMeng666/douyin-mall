# Spring Boot项目数据库本地开发环境与云服务器配置指南

## 1. 前置准备

### 1.1 下载所需软件

- MySQL 8.0: https://dev.mysql.com/downloads/mysql/
- Redis for Windows: https://github.com/microsoftarchive/redis/releases
- IDE: IntelliJ IDEA (推荐)

### 1.2 项目结构了解

本项目为抖音商城项目,使用Spring Boot框架,包含以下重要配置文件:
- application-dev.yml: 开发环境配置文件
- RedisClientConfig.java: Redis配置类
- RedisClientConfigProperties.java: Redis属性配置类
- Constants.java: 常量定义类

## 2. MySQL配置

### 2.1 安装MySQL

1. 运行MySQL安装包
2. 选择"Developer Default"安装类型
3. 配置root密码
4. 完成安装

### 2.2 创建项目数据库

1. 打开命令提示符(管理员):
```bash
mysql -u root -p
# 输入你设置的密码
```

2. 创建数据库:
```sql
CREATE DATABASE IF NOT EXISTS douyin_mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE douyin_mall;
```

### 2.3 配置本地MySQL连接

在application-dev.yml中配置:

```yaml
spring:
  datasource:
    username: root
    password: {你的MySQL密码}  # 替换为你的密码
    url: jdbc:mysql://127.0.0.1:3306/douyin_mall?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&serverTimezone=UTC&useSSL=true
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.zaxxer.hikari.HikariDataSource

  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true

  hikari:
    pool-name: Retail_HikariCP
    minimum-idle: 15 
    maximum-pool-size: 25
    idle-timeout: 180000 #空闲连接存活最大时间，默认600000（10分钟）
    max-lifetime: 1800000 #连接的最长生命周期，默认1800000即30分钟
    connection-timeout: 60000 #连接超时时间,默认30秒，即30000
    auto-commit: true  
    connection-test-query: SELECT 1
```

## 3. Redis配置

### 3.1 安装Redis

1. 运行Redis-x64-xxx.msi
2. 勾选"Add the Redis installation folder to the PATH environment variable"
3. 完成安装后验证:
```bash
# 打开新的命令提示符
redis-cli
127.0.0.1:6379> ping
# 显示PONG则安装成功
```

### 3.2 配置Redis连接

在application-dev.yml中添加:

```yaml
redis:
  sdk:
    config:
      host: 127.0.0.1
      port: 6379
      pool-size: 10
      min-idle-size: 5
      idle-timeout: 30000
      connect-timeout: 5000
      retry-attempts: 3
      retry-interval: 1000
      ping-interval: 60000
      keep-alive: true
```

### 3.3 实现Redis测试接口

1. 创建Redis测试Controller (src/main/java/com/qxy/controller/RedisTestController.java):

```java
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
```

2. 在Constants类中添加Redis key常量 (src/main/java/com/qxy/common/constant/Constants.java):

```java
public class Constants {
    // ... 其他常量代码 ...

    public static class RedisKey {
        public static final String TEST_PREFIX = "test:";
        public static final String USER_PREFIX = "user:";
        public static final String PRODUCT_PREFIX = "product:";
        public static final String ORDER_PREFIX = "order:";
        
        public static String getKey(String prefix, String key) {
            return prefix + key;
        }
    }
}
```

## 4. 云服务器数据库配置

### 4.1 连接信息（重要）

本项目云服务器数据库连接信息:
- 服务器IP: 116.62.149.227
- 数据库端口: 13306
- 用户名: root
- 密码: 123456

### 4.2 修改配置文件

修改application-dev.yml中的数据源配置:

```yaml
spring:
  datasource:
    username: root    # 云服务器数据库用户名
    password: 123456  # 云服务器数据库密码
    url: jdbc:mysql://116.62.149.227:13306/douyin_mall?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&serverTimezone=UTC&useSSL=true
```

### 4.3 测试云数据库连接

1. 命令行测试:
```bash
mysql -h 116.62.149.227 -P 13306 -u root -p123456
```

2. 查看数据库:
```sql
show databases;
use douyin_mall;
show tables;
```

## 5. 实体类配置

### 5.1 修改AiOrder实体类

修改src/main/java/com/qxy/model/po/AiOrder.java:

```java
package com.qxy.model.po;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ai_order")  // 避免使用MySQL关键字
public class AiOrder {
    @Id
    private String orderId;
    private String status;

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
```

## 6. 测试验证

### 6.1 启动项目

1. 确保服务运行:
```bash
# 检查MySQL服务
net start mysql

# 检查Redis服务
redis-cli ping
```

2. 运行Application.java

3. 观察启动日志,确认无错误信息

### 6.2 测试接口

1. Redis测试:
- 访问 http://localhost:8080/api/redis/test
- 正确响应格式:
```json
{
    "code": "0000",
    "info": "调用成功",
    "data": "Redis测试成功! value=Redis connection test, count=1"
}
```

2. MySQL连接测试:
```sql
mysql -h 116.62.149.227 -P 13306 -u root -p123456
use douyin_mall;
show tables;
# 检查ai_order表是否存在
```

## 7. 常见问题处理

### 7.1 MySQL连接问题

1. Access denied错误:
- 检查用户名密码是否正确
- 确认用户是否有对应数据库的权限

2. Communications link failure:
- 检查网络连接
- 确认MySQL服务是否运行
- 检查防火墙设置

### 7.2 Redis连接问题

1. 连接超时:
- 确认Redis服务运行状态
- 检查redis.conf配置
- 验证端口是否被占用

2. 数据操作失败:
- 使用redis-cli测试基本操作
- 检查内存使用情况
- 查看Redis日志

### 7.3 实体类映射问题

如果遇到表名错误:
```sql
# 登录MySQL删除错误表
mysql -h 116.62.149.227 -P 13306 -u root -p123456
use douyin_mall;
DROP TABLE IF EXISTS `order`;
```

然后重启应用,让Hibernate重新创建正确的表结构。

## 8. 配置验证清单

完整配置后,确认以下内容:

1. 基础配置
- [ ] MySQL服务运行正常
- [ ] Redis服务运行正常
- [ ] application-dev.yml配置正确

2. 数据库连接
- [ ] 可以通过命令行连接云数据库
- [ ] 项目可以正常连接数据库
- [ ] 数据表结构正确创建

3. Redis功能
- [ ] Redis测试接口返回成功
- [ ] 可以正常存取数据
- [ ] 连接池工作正常

4. 应用状态
- [ ] 启动无错误日志
- [ ] 接口调用正常
- [ ] 数据操作正确

如果以上任一项检查失败,请参考对应章节进行问题排查和修复。