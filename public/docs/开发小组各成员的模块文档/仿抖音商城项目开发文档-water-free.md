# 仿抖音商城项目开发文档

### 版本信息
- 当前版本: `v1.0.0`
- 发布日期: `2025-01-18`

### **认证中心**

#### 1. 分发身份令牌

- **URL**: `/api/auth/token`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```

- **响应**:

  ```
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

#### 2. 续期身份令牌（高级）

- **URL**: `/api/auth/token/renew`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

- **响应**:

  ```
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

#### 3. 校验身份令牌

- **URL**: `/api/auth/token/validate`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

- **响应**:

  ```
  {
    "valid": true
  }
  ```

------

### **用户服务**

#### 1. 创建用户

- **URL**: `/api/user/register`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "email": "user@example.com",
    "password": "password123",
    "confirmPassword": "password123"
  }
  ```

- **响应**:

  ```
  {
    "userId": 1
  }
  ```

#### 2. 登录

- **URL**: `/api/user/login`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```

- **响应**:

  ```
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

#### 3. 用户登出（可选）

- **URL**: `/api/user/logout`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

- **响应**:

  ```
  {
    "success": true
  }
  ```

#### 4. 删除用户（可选）

- **URL**: `/api/user/delete`

- **HTTP 方法**: DELETE

- **请求体**:

  ```
  {
    "userId": 1
  }
  ```

- **响应**:

  ```
  {
    "success": true
  }
  ```

------

### **商品服务**

#### 1. 创建商品（可选）

- **URL**: `/api/product/create`

- **HTTP 方法**: POST

- **请求体**: form-data

  ```
  param(Text): {
  "name":"测试",
   "description":"测试",
   "price":"100.0",
   "stock":"100"
   }
  productPic(File): 上传图片文件 
  ```

- **响应**:

  ```
  {
    "productId": 1
  }
  ```

#### 2. 修改商品信息（可选）

- **URL**: `/api/product/update`

- **HTTP 方法**: PUT

- **请求体**:

  ```
  {
    "productId": 1,
    "name": "Updated Product Name",
    "description": "Updated Product Description",
    "price": 150.0
  }
  ```

- **响应**:

  ```
  {
    "success": true
  }
  ```

#### 3. 删除商品（可选）

- **URL**: `/api/product/delete`

- **HTTP 方法**: DELETE

- **请求体**:

  ```
  {
    "productId": 1
  }
  ```

- **响应**:

  ```
  {
    "success": true
  }
  ```

#### 4. 查询商品信息（单个商品、批量商品）

- **URL**: `/api/product/get`

- **HTTP 方法**: GET

- **请求参数**:

  - `productId` (单个商品查询)
  - `productIds` (批量商品查询，逗号分隔)

- **响应**:

  ```
  {
    "products": [
      {
        "productId": 1,
        "name": "Product Name",
        "description": "Product Description",
        "price": 100.0
      }
    ]
  }
  ```

------

### **购物车服务**

#### 1. 创建购物车

- **URL**: `/api/cart/create`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "userId": 1
  }
  ```

- **响应**:

  ```
  {
    "cartId": 1
  }
  ```

#### 2. 清空购物车

- **URL**: `/api/cart/clear`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "cartId": 1
  }
  ```

- **响应**:

  ```
  {
    "success": true
  }
  ```

#### 3. 获取购物车信息

- **URL**: `/api/cart/get`

- **HTTP 方法**: GET

- **请求参数**:

  - `cartId`

- **响应**:

  ```
  {
    "cartId": 1,
    "items": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }
  ```

------

### **订单服务**

#### 1. 创建订单

- **URL**: `/api/order/create`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "userId": 1,
    "cartId": 1
  }
  ```

- **响应**:

  ```
  {
    "orderId": 1
  }
  ```

#### 2. 修改订单信息（可选）

- **URL**: `/api/order/update`

- **HTTP 方法**: PUT

- **请求体**:

  ```
  {
    "orderId": 1,
    "status": "SHIPPED"
  }
  ```

- **响应**:

  ```
  {
    "success": true
  }
  ```

#### 3. 订单定时取消（高级）

- **URL**: `/api/order/cancel`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "orderId": 1
  }
  ```

- **响应**:

  ```
  {
    "success": true
  }
  ```

------

### **结算**

#### 1. 订单结算

- **URL**: `/api/checkout`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "orderId": 1
  }
  ```

- **响应**:

  ```
  {
    "success": true
  }
  ```

------

### **支付**

#### 1. 取消支付（高级）

- **URL**: `/api/payment/cancel`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "paymentId": 1
  }
  ```

- **响应**:

  ```
  {
    "success": true
  }
  ```

#### 2. 定时取消支付（高级）

- **URL**: `/api/payment/cancel/scheduled`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "paymentId": 1
  }
  ```

- **响应**:

  ```
  {
    "success": true
  }
  ```

#### 3. 支付

- **URL**: `/api/payment/pay`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "orderId": 1,
    "paymentMethod": "CREDIT_CARD"
  }
  ```

- **响应**:

  ```
  {
    "success": true
  }
  ```

------

### **AI 大模型**

#### 1. 订单查询

- **URL**: `/api/ai/order/query`

- **HTTP 方法**: GET

- **请求参数**:

  - `orderId`

- **响应**:

  ```
  {
    "orderId": 1,
    "status": "PAID"
  }
  ```

#### 2. 模拟自动下单

- **URL**: `/api/ai/order/auto`

- **HTTP 方法**: POST

- **请求体**:

  ```
  {
    "userId": 1
  }
  ```

- **响应**:

  ```
  {
    "orderId": 1
  }
  ```

