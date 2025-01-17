# Douyin Mall Java 抖音商城项目

## 项目介绍
本项目是由"重生之我在青训营敲代码"团队（队伍编号：115）开发的一个完整的电商平台系统。项目采用现代化的技术栈和微服务架构，实现了用户管理、商品管理、购物车、订单处理等核心功能。

## 功能模块
### 认证中心
- 实现身份令牌的分发功能
- 提供续期和校验功能
- 支持多种认证方式

### 用户信息模块
- 用户注册
- 多方式登录（验证码登录、密码登录）
- 用户信息管理
- 账号注销功能

### 商品信息模块
- 商品的创建、删除、修改
- 商品信息查询
- 商品展示功能

### 购物车模块
- 购物车的创建
- 商品的添加与删除
- 购物车查询功能

### 订单模块
- 订单创建和管理
- 订单取消功能
- 超时订单自动取消
- 订单状态流转（已完成、待支付等）

### 结算与支付
- 订单结算功能
- 支付功能
- 支付取消功能

## 技术选型
- **后端框架**：Spring Boot
- **数据存储**：MySQL + Redis
- **消息队列**：RabbitMQ
- **安全框架**：JWT + Spring Security
- **容器化**：Docker
- **云服务**：Spring Alibaba Cloud
- **AI集成**：支持接入大型AI模型

## 项目成员
- water：文档编写、数据库设计、AI模型接入
- 苏泰宇：认证中心开发
- chan：购物车模块开发
- 邓申桐：订单模块开发
- 魏鑫：结算与支付模块开发

## 开发环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Redis 6.0+
- RabbitMQ 3.8+
- Docker 20.10+

## 快速开始
```bash
# 克隆项目
git clone https://github.com/ChanMeng666/douyin-mall.git

# 进入项目目录
cd douyin-mall

# 安装依赖
mvn install

# 启动项目
mvn spring-boot:run
```

## 项目结构
```
douyin-mall/
├── auth-center/        # 认证中心
├── user-service/       # 用户服务
├── product-service/    # 商品服务
├── cart-service/       # 购物车服务
├── order-service/      # 订单服务
├── payment-service/    # 支付服务
├── common/            # 公共模块
└── docs/              # 项目文档
```

## 部署说明
本项目支持使用Docker进行容器化部署，详细部署步骤请参考`docs/deployment.md`文件。

## 贡献指南
1. Fork 本仓库
2. 创建新的分支: `git checkout -b feature/xxx`
3. 提交更改: `git commit -am 'Add some feature'`
4. 推送到分支: `git push origin feature/xxx`
5. 提交 Pull Request

## 开源协议
本项目遵循 Apache-2.0 协议。
