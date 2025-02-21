# 抖音商城项目-AI模块文档-water

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

---

## 4. 当前潜在问题及优化方案

### 4.1 潜在问题
1. **AI 解析准确性不足**：
   - 用户输入可能存在歧义，导致 AI 无法准确解析商品名称或数量。
   - 解析失败时缺乏明确的提示信息，用户体验较差。

2. **并发问题**：
   - 在高并发场景下，可能出现库存超卖问题（多个用户同时下单同一商品）。
   - 数据库事务隔离级别设置不当可能导致脏读或幻读。

3. **性能瓶颈**：
   - 商品搜索和库存更新操作可能成为性能瓶颈，特别是在商品数量较多时。
   - 缺乏缓存机制，频繁访问数据库会增加负载。

4. **安全性问题**：
   - 接口未对敏感数据（如用户 ID）进行加密传输，存在安全隐患。
   - 缺乏限流和防刷机制，可能被恶意攻击。

5. **扩展性不足**：
   - 当前代码耦合度较高，新增功能时需要修改多处代码。
   - AI 服务调用逻辑直接嵌入业务代码中，难以替换其他 AI 服务。

### 4.2 正式上线可能出现的问题
1. **高峰期系统崩溃**：
   - 高并发场景下，数据库连接池耗尽或服务器资源不足，可能导致系统不可用。
2. **用户体验不佳**：
   - AI 解析失败或下单失败时，用户可能感到困惑，缺乏友好的错误提示。
3. **监控和日志不足**：
   - 缺乏完善的监控和日志记录，出现问题时难以定位原因。

### 4.3 可优化的地方及优化方案
1. **提升 AI 解析准确性**：
   - 增加用户输入的预处理逻辑，例如关键词提取和语义分析。
   - 提供用户确认步骤，让用户核对解析结果后再提交订单。

2. **解决并发问题**：
   - 使用分布式锁（如 Redis 锁）控制库存扣减操作，避免超卖。
   - 设置数据库事务隔离级别为可重复读（Repeatable Read），防止并发问题。

3. **优化性能**：
   - 引入 Redis 缓存，存储热门商品信息和库存状态，减少数据库压力。
   - 对商品搜索接口进行分页处理，避免一次性加载过多数据。

4. **增强安全性**：
   - 对敏感数据进行加密传输（如 HTTPS）。
   - 添加限流和防刷机制，限制每个用户的请求频率。

5. **提高扩展性**：
   - 将 AI 服务调用逻辑抽象为独立模块，便于替换或升级。
   - 使用设计模式（如策略模式）解耦业务逻辑，提高代码灵活性。

---

## 5. 反思与总结

### 5.1 成功之处
- 项目实现了 AI 智能化下单和查询功能，提升了用户体验。
- 代码结构清晰，分层设计合理，便于维护和扩展。
- 使用了主流技术栈（Spring Boot、MyBatis、MySQL），保证了系统的稳定性和可靠性。

### 5.2 不足之处
- 对高并发场景的考虑不足，可能导致系统性能下降。
- AI 解析逻辑较为简单，无法应对复杂的用户输入。
- 缺乏完善的监控和日志记录，不利于问题排查。

### 5.3 总结与展望
本项目是一个初步的尝试，虽然实现了基本功能，但在性能、安全性和扩展性方面仍有较大的优化空间。未来可以引入更多先进技术（如微服务架构、分布式缓存、AI 模型优化等），进一步提升系统的稳定性和用户体验。同时，加强监控和日志记录，确保系统能够快速响应和解决问题。