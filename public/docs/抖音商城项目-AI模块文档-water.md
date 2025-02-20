# 抖音商城项目文档

## 1. 技术选型与相关开发文档

### 1.1 技术栈
本项目基于 Java 技术栈，结合 Spring Boot 框架进行开发，使用了以下主要技术和工具：
- **Spring Boot**：作为后端框架，提供快速开发和配置管理能力。
- **MyBatis**：作为持久层框架，用于数据库操作。
- **MySQL**：作为关系型数据库，存储订单、商品等数据。
- **Lombok**：简化实体类的代码编写，减少样板代码。
- **Hibernate Validator**：用于参数校验，确保接口输入数据的合法性。
- **通义千问（Qwen AI）**：用于解析用户意图，实现智能化下单和查询功能。
- **Maven**：作为构建工具，管理依赖和项目打包。

### 1.2 开发文档
#### 1.2.1 接口文档
##### AI 下单接口
- **URL**: `/api/ai/order/auto`
- **Method**: POST
- **Request Body**:
  ```json
  {
    "message": "我想买两部手机",
    "userId": 12345
  }
  ```
- **Response**:
  ```json
  {
    "paymentUrl": "https://example.com/pay?orderId=1001",
    "orderId": "1001",
    "status": "待支付"
  }
  ```

##### AI 查询订单接口
- **URL**: `/api/ai/order/query`
- **Method**: POST
- **Request Params**:
  - `userInput`: 用户输入内容，例如“我的订单”或“订单ID是1001”。
- **Response**:
  ```
  订单ID: 1001, 用户ID: 12345, 总金额: 9999.99, 状态: 已完成
  ```

#### 1.2.2 数据库设计
- **orders 表**：存储订单信息。
  ```sql
  CREATE TABLE orders (
      order_id INT PRIMARY KEY AUTO_INCREMENT,
      user_id INT NOT NULL,
      total_amount DECIMAL(10, 2) NOT NULL,
      status VARCHAR(50),
      pay_type INT,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  );
  ```
- **order_items 表**：存储订单项信息。
  ```sql
  CREATE TABLE order_items (
      id INT PRIMARY KEY AUTO_INCREMENT,
      order_id INT NOT NULL,
      product_id INT NOT NULL,
      quantity INT NOT NULL,
      price DECIMAL(10, 2) NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  );
  ```
- **products 表**：存储商品信息。
  ```sql
  CREATE TABLE products (
      product_id INT PRIMARY KEY AUTO_INCREMENT,
      name VARCHAR(255) NOT NULL,
      description TEXT,
      price DECIMAL(10, 2) NOT NULL,
      stock INT NOT NULL,
      image_url VARCHAR(255),
      status VARCHAR(50) DEFAULT 'active',
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  );
  ```

---

## 2. 架构设计

### 2.1 系统架构
系统采用典型的三层架构设计：
1. **Controller 层**：负责接收 HTTP 请求，调用 Service 层处理业务逻辑，并返回响应。
2. **Service 层**：实现核心业务逻辑，包括调用 AI 服务解析用户意图、操作数据库等。
3. **DAO 层**：负责与数据库交互，执行 CRUD 操作。

### 2.2 流程设计
#### AI 下单流程
1. 用户通过接口发送下单请求，包含用户消息和用户 ID。
2. Controller 层接收到请求后，将数据传递给 Service 层。
3. Service 层调用通义千问 AI 服务解析用户意图，提取商品名称和购买数量。
4. 根据解析结果，查询商品库存并选择价格最低的商品。
5. 创建订单记录和订单项记录，扣减库存。
6. 返回支付链接和订单状态。

#### AI 查询订单流程
1. 用户通过接口发送查询请求，包含用户输入内容。
2. Controller 层接收到请求后，调用 Service 层。
3. Service 层调用通义千问 AI 服务解析用户输入，提取用户 ID 或订单 ID。
4. 根据提取的信息，查询数据库中的订单记录。
5. 返回订单详情。

### 2.3 异常处理
- 使用自定义异常类 `BusinessException` 处理业务逻辑中的异常。
- 在 Service 层捕获异常并抛出，由全局异常处理器统一返回错误信息。

---

## 3. 项目代码介绍

### 3.1 DTO 类
- **AiOrderRequestDTO**：用于封装 AI 下单请求参数。
  ```java
  @Data
  public class AiOrderRequestDTO {
      @NotBlank(message = "用户消息不能为空")
      private String message;
  
      @NotNull(message = "用户ID不能为空")
      private Integer userId;
  }
  ```

- **AiOrderResponseDTO**：用于封装 AI 下单响应结果。
  ```java
  @Data
  public class AiOrderResponseDTO {
      private String paymentUrl;
      private String orderId;
      private String status;
  }
  ```

### 3.2 Controller 类
- **ChatController**：提供 AI 下单和查询订单的接口。
  ```java
  @RestController
  @RequestMapping("/api/ai/order")
  public class ChatController {
  
      @Autowired
      private AIServiceImpl aiServiceImpl;
  
      @Autowired
      private AISimulationOrderService aiSimulationOrderService;
  
      @PostMapping("/query")
      public String askAI(@RequestParam String userInput) {
          return aiServiceImpl.handleUserInput(userInput);
      }
  
      @PostMapping("/auto")
      public ResponseEntity createOrder(
              @RequestBody @Valid AiOrderRequestDTO requestDTO) {
          AiOrderResponseDTO response = aiSimulationOrderService.createOrderFromMessage(requestDTO);
          return ResponseEntity.ok(response);
      }
  }
  ```

### 3.3 Service 类
- **AISimulationOrderServiceImpl**：实现 AI 下单的核心逻辑。
  ```java
  @Service
  public class AISimulationOrderServiceImpl implements AISimulationOrderService {
  
      @Autowired
      private AiProductDao aiProductMapper;
  
      @Autowired
      private AiOrderDao aiOrderMapper;
  
      @Override
      @Transactional
      public AiOrderResponseDTO createOrderFromMessage(AiOrderRequestDTO requestDTO) {
          // 调用 AI 解析用户意图，创建订单，扣减库存，返回支付链接
      }
  }
  ```

- **AIServiceImpl**：实现 AI 查询订单的核心逻辑。
  ```java
  @Service
  public class AIServiceImpl {
  
      @Autowired
      private AiOrderService orderService;
  
      @Autowired
      private QwenAIService qwenAIService;
  
      public String handleUserInput(String userInput) {
          // 调用 AI 解析用户输入，查询订单并返回结果
      }
  }
  ```

### 3.4 DAO 类
- **AiOrderDao**：操作订单表。
  ```java
  @Mapper
  public interface AiOrderDao {
      List findByUserId(@Param("userId") Integer userId);
      AiOrder findByOrderId(@Param("orderId") Integer orderId);
      void insertOrder(AiOrder order);
      void insertOrderItem(OrderItems orderItem);
  }
  ```

- **AiProductDao**：操作商品表。
  ```java
  @Mapper
  public interface AiProductDao {
      List searchProductsByName(@Param("keyword") String keyword);
      void decreaseStock(@Param("productId") Integer productId, @Param("quantity") Integer quantity);
  }
  ```

### 3.5 实体类
- **AiOrder**：表示订单实体。
  ```java
  @Data
  public class AiOrder {
      private Integer orderId;
      private Integer userId;
      private BigDecimal totalAmount;
      private String status;
      private Integer payType;
      private Timestamp createdAt;
      private Timestamp updatedAt;
  }
  ```

- **Product**：表示商品实体。
  ```java
  @Data
  public class Product {
      private Integer productId;
      private String name;
      private String description;
      private BigDecimal price;
      private Integer stock;
      private Timestamp createdAt;
      private Timestamp updatedAt;
      private String imageUrl;
      private String status;
  }
  ```

---

## 总结
本项目通过整合 Spring Boot、MyBatis 和通义千问 AI，实现了抖音商城的 AI 模拟下单和查询订单功能。系统设计清晰，代码结构合理，具备良好的扩展性和可维护性。