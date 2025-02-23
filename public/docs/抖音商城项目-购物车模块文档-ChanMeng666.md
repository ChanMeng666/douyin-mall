# 购物车模块分析文档

## 1. 模块概述

购物车模块是电商系统的核心功能之一，负责管理用户的购物车信息，包括添加商品、更新数量、删除商品等基础操作。本项目实现了一个基于Spring Boot的购物车系统。

## 2. 核心功能实现

### 2.1 数据模型设计

#### 购物车主表(carts)
```sql
CREATE TABLE carts (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX (user_id)
);
```

#### 购物车商品表(cart_items)
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

### 2.2 核心类结构

- **控制层**: CartController
- **服务层**: CartService (接口) 和 CartServiceImpl (实现)
- **数据访问层**: CartDao 和 CartItemDao
- **实体类**: Cart 和 CartItem
- **DTO类**: CartDTO, CartItemDTO, CartResponseDTO 等

### 2.3 主要功能实现

#### 创建购物车
```java
@Override
@Transactional
public void createCart(Integer userId) {
    Cart cart = new Cart();
    cart.setUserId(userId);
    cartRepository.createCart(cart);
}
```

#### 添加商品到购物车
```java
@Override
@Transactional
public void addItem(Integer userId, Integer productId, Integer quantity) {
    // 验证数量
    if (quantity <= 0) {
        throw new AppException(ResponseCode.UN_ERROR.getCode(), "Invalid quantity");
    }

    // 获取或创建购物车
    Cart cart = cartRepository.getCartByUserId(userId);
    if (cart == null) {
        createCart(userId);
        cart = cartRepository.getCartByUserId(userId);
    }

    // 检查商品是否存在
    ProductRes product = productService.getProductById(productId);
    if (product == null) {
        throw new AppException(ResponseCode.UN_ERROR.getCode(), "Product not found");
    }

    // 更新或创建购物车项
    CartItem existingItem = cartItemRepository.getItemByProductId(cart.getCartId(), productId);
    if (existingItem != null) {
        // 更新已存在的商品数量
        int newQuantity = existingItem.getQuantity() + quantity;
        existingItem.setQuantity(newQuantity);
        existingItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(newQuantity)));
        cartItemRepository.updateItem(existingItem);
    } else {
        // 创建新的购物车项
        CartItem newItem = CartItem.builder()
            .cartId(cart.getCartId())
            .productId(productId)
            .quantity(quantity)
            .totalPrice(product.getPrice().multiply(new BigDecimal(quantity)))
            .build();
        cartItemRepository.insertItem(newItem);
    }
}
```

## 3. 存在的问题

### 3.1 并发控制问题
- 缺少商品库存的并发控制机制
- 多用户同时操作同一购物车时可能产生数据不一致
- 事务隔离级别未明确指定

### 3.2 性能问题
- 缺少缓存机制，频繁访问数据库
- 未实现购物车商品的批量操作
- 大量购物车项时的性能优化不足

### 3.3 业务逻辑问题
- 未处理商品下架情况
- 缺少购物车商品数量上限控制
- 未实现商品失效自动处理机制

## 4. 优化建议

### 4.1 并发控制优化
```java
// 建议添加分布式锁
@Transactional
public void addItem(Integer userId, Integer productId, Integer quantity) {
    String lockKey = "cart:lock:" + userId;
    try {
        if (redissonClient.tryLock(lockKey, 1, 5, TimeUnit.SECONDS)) {
            // 现有的添加商品逻辑
        }
    } finally {
        redissonClient.unlock(lockKey);
    }
}
```

### 4.2 性能优化
1. 添加Redis缓存层
2. 实现购物车商品批量操作接口
3. 引入商品库存预检机制

### 4.3 业务逻辑优化
1. 添加商品状态检查
2. 实现定时清理失效商品
3. 增加购物车容量限制

## 5. 反思与总结

### 5.1 设计亮点
1. 清晰的分层架构
2. 完整的事务管理
3. 良好的异常处理机制

### 5.2 不足之处
1. 缺少缓存策略
2. 并发控制不足
3. 业务边界处理不完善

### 5.3 改进方向
1. 引入分布式缓存
2. 完善并发控制
3. 增加更多业务场景支持
4. 优化性能监控机制

## 6. 代码规范建议

### 6.1 异常处理规范
```java
public void addItem(Integer userId, Integer productId, Integer quantity) {
    try {
        // 业务逻辑
    } catch (Exception e) {
        log.error("Add cart item failed: userId={}, productId={}", userId, productId, e);
        throw new AppException(ResponseCode.UN_ERROR.getCode(), "Add cart item failed");
    }
}
```

### 6.2 日志规范
```java
@Slf4j
public class CartServiceImpl {
    public void addItem(...) {
        log.info("Adding item to cart: userId={}, productId={}, quantity={}", userId, productId, quantity);
        // 业务逻辑
        log.info("Successfully added item to cart: cartItemId={}", cartItem.getCartItemId());
    }
}
```

## 7. 未来展望

1. 引入商品推荐系统
2. 实现购物车商品价格变动提醒
3. 添加购物车商品库存预警
4. 支持多端购物车数据同步
5. 实现购物车商品关联推荐

## 8. 测试用例建议

建议补充以下测试场景：
1. 高并发场景测试
2. 大数据量性能测试
3. 异常场景恢复测试
4. 边界条件测试
5. 接口安全性测试
