# **仿抖音商城项目代码风格规范书**

---

#### **1. 目的**
为了确保项目代码的可读性、可维护性和一致性，制定本代码风格规范书。所有开发人员需严格遵守本规范。

---

#### **2. 代码风格规范**

##### **2.1 命名规范**
1. **类名**：
   
   - 使用大驼峰命名法（PascalCase）。
   - 类名应具有描述性，避免缩写。
   - 示例：
     ```java
     public class UserService { ... }
     ```
   
2. **方法名**：
   - 使用小驼峰命名法（camelCase）。
   - 方法名应清晰表达其功能，避免缩写。
   - 示例：
     ```java
     public void createUser(String username) { ... }
     ```

3. **变量名**：
   - 使用小驼峰命名法（camelCase）。
   - 变量名应具有描述性，避免单字母命名（除非是循环变量）。
   - 示例：
     ```java
     int userCount;
     String userName;
     ```

4. **常量名**：
   - 使用全大写字母，单词间用下划线分隔。
   - 示例：
     ```java
     public static final int MAX_RETRY_COUNT = 3;
     ```

5. **包名**：
   - 使用全小写字母，单词间用点号分隔。
   - 包名应反映模块或功能。
   - 示例：
     ```java
     com.tiktokmall.user;
     com.tiktokmall.order;
     ```

---

##### **2.2 代码格式**
1. **缩进**：
   - 使用 4 个空格进行缩进。
   - 示例：
     ```java
     public void exampleMethod() {
         if (condition) {
             System.out.println("Hello, World!");
         }
     }
     ```

2. **花括号**：
   - 左花括号 `{` 与语句同行，右花括号 `}` 单独成行。
   - 示例：
     ```java
     if (condition) {
         // code
     } else {
         // code
     }
     ```

3. **空格**：
   - 运算符两侧、逗号后、冒号后需加空格。
   - 示例：
     ```java
     int sum = a + b;
     for (int i = 0; i < 10; i++) { ... }
     ```

4. **行长度**：
   - 每行代码不超过 120 个字符。
   - 超长代码应换行，换行后缩进 8 个空格。
   - 示例：
     ```java
     String longString = "This is a very long string that needs to be split "
             + "into multiple lines for better readability.";
     ```

5. **空行**：
   - 方法之间用空行分隔。
   - 逻辑相关的代码块之间用空行分隔。
   - 示例：
     ```java
     public void methodA() {
         // code
     }
     
     public void methodB() {
         // code
     }
     ```

---

##### **2.3 注释规范**
1. **类注释**：
   - 每个类需添加类注释，说明类的功能和用途。
   - 示例：
     ```java
     /**
      * 用户服务类，负责用户相关的业务逻辑。
      */
     public class UserService { ... }
     ```

2. **方法注释**：
   - 每个方法需添加方法注释，说明方法的功能、参数和返回值。
   - 示例：
     ```java
     /**
      * 创建用户。
      *
      * @param username 用户名
      * @return 创建的用户ID
      */
     public int createUser(String username) { ... }
     ```

3. **行内注释**：
   - 在代码逻辑复杂的地方添加行内注释，解释代码的作用。
   - 示例：
     ```java
     int result = a + b; // 计算 a 和 b 的和
     ```

---

##### **2.4 异常处理**
1. **捕获异常**：
   - 捕获异常时需明确处理逻辑，避免空捕获。
   - 示例：
     ```java
     try {
         // code
     } catch (IOException e) {
         log.error("IO异常", e);
         throw new RuntimeException("处理失败", e);
     }
     ```

2. **自定义异常**：
   - 自定义异常需继承 `RuntimeException` 或 `Exception`。
   - 示例：
     ```java
     public class UserNotFoundException extends RuntimeException {
         public UserNotFoundException(String message) {
             super(message);
         }
     }
     ```

---

##### **2.5 日志规范**
1. **日志级别**：
   - 使用合适的日志级别：
     - `DEBUG`：调试信息。
     - `INFO`：重要业务信息。
     - `WARN`：警告信息。
     - `ERROR`：错误信息。
   - 示例：
     ```java
     log.info("用户创建成功，用户名：{}", username);
     log.error("用户创建失败", e);
     ```

2. **日志格式**：
   - 日志需包含上下文信息，便于排查问题。
   - 示例：
     ```java
     log.info("订单创建成功，订单ID：{}，用户ID：{}", orderId, userId);
     ```

---

##### **2.6 测试规范**
1. **单元测试**：
   - 每个方法需编写单元测试，确保功能正确。
   - 使用 JUnit 进行单元测试。
   - 示例：
     ```java
     @Test
     public void testCreateUser() {
         UserService userService = new UserService();
         int userId = userService.createUser("testUser");
         assertNotNull(userId);
     }
     ```

2. **集成测试**：
   - 对模块间的交互进行集成测试，确保系统整体功能正常。

---

#### **3. 工具与配置**
1. **IDE 配置**：
   - 使用 IntelliJ IDEA ，配置统一的代码格式化模板。
3. **Git 规范**：
   - 使用 Git Flow 分支管理策略。
   - 提交信息需清晰描述改动内容。

---

#### **4. 代码审查**
1. **审查流程**：
   - 每次提交代码前需进行代码审查，有不会的不懂的不确定的，一定要先发到群里问！！！
   - 审查内容包括代码风格、逻辑正确性、性能优化等。

---

#### **5. 附录**
1. **参考文档**：
   - [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
   - [Oracle Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-introduction.html)

---

希望这份代码风格规范书能帮助大家高效完成项目！如果还有其他问题，请随时告诉我！ 😊