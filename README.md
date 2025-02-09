# 抖音商城项目(后端)

> [!IMPORTANT]
> 本项目是由"重生之我在青训营敲代码"团队（队伍编号：115）开发

## 项目简介
抖音商城是一个基于Spring Boot的电商平台后端系统,采用前后端分离架构。后端提供RESTful API接口,前端使用Vue3进行开发。本项目实现了完整的电商业务流程,包括用户管理、商品管理、购物车、订单处理等核心功能。

## 系统架构
### 技术选型
- 开发语言：Java 8
- 项目框架：Spring Boot 2.7.12
- 数据存储：MySQL 8.0 + Redis 6.0
- 项目管理：Maven 3.6+
- 数据库连接池：HikariCP
- ORM框架：MyBatis
- 安全框架：Spring Security + JWT
- 分布式缓存：Redisson
- 定时任务：Spring Task

### 数据库设计
#### 主要数据表
1. 用户表(users)
```sql
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('active', 'inactive') DEFAULT 'active'
);
```

2. 商品表(products)
```sql
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL,
    image_url varchar(2048) DEFAULT NULL COMMENT '商品展示图',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('active', 'inactive') DEFAULT 'active'
);
```

3. 购物车表(carts)
```sql
CREATE TABLE carts (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX (user_id)
);
```

4. 购物车商品表(cart_items)
```sql
CREATE TABLE cart_items (
    cart_item_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX (cart_id),
    INDEX (product_id)
);
```

5. 订单表(orders)
```sql
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('PAY_WAIT', 'HAVE_PAID', 'CANCELLED') DEFAULT 'PAY_WAIT',
    pay_type VARCHAR(20),
    pay_time TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 核心功能模块

### 1. 商品管理模块
#### 主要接口
- 创建商品: POST `/api/product/create`
- 修改商品: PUT `/api/product/update`
- 删除商品: DELETE `/api/product/delete`
- 查询商品: GET `/api/product/get`
- 商品图片上传: POST `/api/product/pic/upload`

#### 示例请求
创建商品:
```json
POST /api/product/create
Content-Type: multipart/form-data

param: {
    "name": "测试商品",
    "description": "商品描述",
    "price": "99.99",
    "stock": "100"
}
productPic: [文件]
```

### 2. 购物车模块
#### 主要接口
- 添加商品: POST `/api/cart/add`
- 移除商品: DELETE `/api/cart/items/{cartItemId}`
- 查询购物车: GET `/api/cart/user/{userId}`

#### 示例请求
添加商品到购物车:
```json
POST /api/cart/add
{
    "userId": 1,
    "productId": 1,
    "quantity": 2
}
```

### 3. 订单模块
#### 主要接口
- 创建订单: POST `/api/order/create`
- 查询历史订单: GET `/api/order/history`
- 取消订单: POST `/api/order/cancel`

#### 示例请求
创建订单:
```json
POST /api/order/create
{
    "userId": 1,
    "cartId": 1,
    "cartItemDtos": [{
        "cartItemId": 1,
        "productId": 1,
        "quantity": 2,
        "totalPrice": 199.98
    }],
    "payType": "ALIPAY"
}
```

## 项目配置与部署

### 1. 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- IDE推荐：IntelliJ IDEA

### 2. 数据库配置
1. 创建数据库:
```sql
CREATE DATABASE douyin_mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

2. 执行初始化SQL脚本:
- 表结构: `/public/docs/mysql/douyin-mall-V1.1.0.sql`
- 测试数据: `/public/docs/mysql/250124_douyin_mall_data_injection.sql`

### 3. Redis配置
修改`application-dev.yml`:
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your_password
    database: 0
```

### 4. 前后端连接配置
1. 后端跨域配置(已在WebConfig中配置):
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

2. 前端API配置:
在前端项目的`.env`文件中配置后端接口地址:
```
VITE_API_BASE_URL=http://localhost:8080/api
```

### 5. 项目启动
1. 克隆项目:
```bash
git clone https://github.com/ChanMeng666/douyin-mall.git
cd douyin-mall
```

2. 修改配置:
编辑`src/main/resources/application-dev.yml`,配置数据库连接:
```yaml
spring:
  datasource:
    username: your_username
    password: your_password
    url: jdbc:mysql://localhost:3306/douyin_mall?useUnicode=true&characterEncoding=utf8
```

3. 启动项目:
```bash
mvn spring-boot:run
```

4. 启动前端项目:
```bash
cd ../douyin-mall-frontend
npm install
npm run dev
```

### 6. 部署说明
#### 开发环境部署
直接使用IDE运行或使用Maven命令:
```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

#### 生产环境部署
1. 打包:
```bash
mvn clean package -Dmaven.test.skip=true
```

2. 运行:
```bash
java -jar -Dspring.profiles.active=prod target/douyin-mall-1.0-SNAPSHOT.jar
```

#### Docker部署
1. 构建镜像:
```bash
docker build -t douyin-mall .
```

2. 运行容器:
```bash
docker run -d -p 8080:8080 \
-e SPRING_PROFILES_ACTIVE=prod \
-e MYSQL_HOST=your_mysql_host \
-e REDIS_HOST=your_redis_host \
douyin-mall
```

## 开发规范
- 代码规范参考：`public/docs/仿抖音商城项目代码风格规范书-water-free.md`
- API接口文档：`public/docs/仿抖音商城项目开发文档-water-free.md`
- 数据流规范：`public/docs/数据流规范示例图/数据流规范示意图.md`

## 项目成员
- water：项目文档、数据库设计、AI模型接入
- 苏泰宇：用户认证中心开发
- chan：购物车模块开发
- 邓申桐：订单系统开发
- 魏鑫：支付模块开发

## 开源协议
本项目采用Apache-2.0协议开源，详情请参阅LICENSE文件。

