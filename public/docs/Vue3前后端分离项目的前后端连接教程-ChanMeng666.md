# Vue3前后端分离项目连接教程

## 1. 创建前端项目

### 1.1 初始化Vue项目

```bash
# 创建前端项目目录
mkdir douyin-mall-frontend
cd douyin-mall-frontend

# 使用Vue CLI创建项目
npm create vue@latest

# 选择以下选项:
✔ Project name: douyin-mall-frontend 
✔ Add TypeScript? Yes
✔ Add JSX Support? No 
✔ Add Vue Router for Single Page Application development? Yes
✔ Add Pinia for state management? Yes
✔ Add Vitest for Unit Testing? Yes
✔ Add Cypress for both Unit and End-to-End testing? Yes
✔ Add ESLint for code quality? Yes
✔ Add Prettier for code formatting? Yes

# 安装依赖
cd douyin-mall-frontend
npm install

# 安装axios用于API请求
npm install axios
```

### 1.2 创建TypeScript类型定义

创建文件 `/src/types/index.ts`:

```typescript
export interface Product {
  product_id: number;
  name: string;
  description: string;
  price: number;
  stock: number;
  status: 'active' | 'inactive';
  created_at: string;
  updated_at: string;
}

export interface ProductResponse {
  code: string;
  info: string;
  data: Product[];
}
```

### 1.3 创建API服务

创建文件 `/src/services/productService.ts`:

```typescript
import axios from 'axios'
import type { ProductResponse } from '../types'

const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

export const productService = {
  async getProducts(): Promise<ProductResponse> {
    const response = await axios.get<ProductResponse>(`${BASE_URL}/product/list`)
    return response.data
  }
}
```

### 1.4 创建Pinia Store

创建文件 `/src/stores/productStore.ts`:

```typescript
import { defineStore } from 'pinia'
import { productService } from '../services/productService'
import type { Product } from '../types'

export const useProductStore = defineStore('product', {
  state: () => ({
    products: [] as Product[],
    loading: false,
    error: null as string | null
  }),
  
  actions: {
    async fetchProducts() {
      this.loading = true
      try {
        const response = await productService.getProducts()
        if (response.code === '0000') {
          this.products = response.data
        } else {
          throw new Error(response.info)
        }
      } catch (error) {
        this.error = error instanceof Error ? error.message : '获取商品列表失败'
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})
```

### 1.5 创建商品列表组件

创建文件 `/src/components/ProductList.vue`:

```vue
<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-2xl font-bold mb-6">商品列表</h1>
    <div v-if="loading" class="text-center">加载中...</div>
    <div v-else-if="error" class="text-center text-red-500">{{ error }}</div>
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <div v-for="product in products" :key="product.product_id" 
           class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="p-4">
          <h2 class="text-xl font-semibold mb-2">{{ product.name }}</h2>
          <p class="text-gray-600 mb-4">{{ product.description }}</p>
          <div class="flex justify-between items-center">
            <span class="text-xl font-bold text-red-500">
              ¥{{ product.price }}
            </span>
            <div class="text-sm text-gray-500">
              库存: {{ product.stock }}
              <span :class="product.status === 'active' ? 'text-green-500' : 'text-red-500'">
                ({{ product.status === 'active' ? '在售' : '下架' }})
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useProductStore } from '../stores/productStore'
import type { Product } from '../types'

const productStore = useProductStore()
const loading = ref(true)
const error = ref<string | null>(null)

const products = computed((): Product[] => productStore.products)

onMounted(async () => {
  try {
    await productStore.fetchProducts()
  } catch (e) {
    error.value = e instanceof Error ? e.message : '加载商品失败'
  } finally {
    loading.value = false
  }
})
</script>
```

### 1.6 配置路由

编辑 `/src/router/index.ts`:

```typescript
import { createRouter, createWebHistory } from 'vue-router'
import ProductList from '@/components/ProductList.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: ProductList
    },
    {
      path: '/products',
      name: 'products',
      component: ProductList
    }
  ]
})

export default router
```

### 1.7 配置环境变量

在项目根目录创建 `.env` 文件:

```
VITE_API_BASE_URL=http://localhost:8080/api
```

## 2. 后端配置

### 2.1 确认数据库连接配置

检查 `/douyin-mall/src/main/resources/application-dev.yml`:

```yaml
spring:
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql://116.62.149.227:13306/douyin_mall?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&serverTimezone=UTC&useSSL=true
    driver-class-name: com.mysql.cj.jdbc.Driver

  hikari:
    pool-name: Retail_HikariCP
    minimum-idle: 15
    idle-timeout: 180000
    maximum-pool-size: 25
    auto-commit: true
    max-lifetime: 1800000
    connection-timeout: 30000
    connection-test-query: SELECT 1
```

### 2.2 确认MyBatis Mapper配置

检查 `/douyin-mall/src/main/java/com/qxy/dao/mapper/ProductMapper.java`:

```java
@Mapper
public interface ProductMapper {
    @Select("select * from products")  // 注意这里是products而不是product
    List<ProductDO> selectAll();
}
```

## 3. 测试连接

1. 启动后端服务
```bash
# 在douyin-mall目录下
./mvnw spring-boot:run
```

2. 启动前端开发服务器
```bash
# 在douyin-mall-frontend目录下
npm run dev
```

3. 访问前端页面
- 打开浏览器访问 `http://localhost:5173`
- 应该能看到商品列表数据
- 使用Chrome开发者工具(F12)检查网络请求，确认API请求状态是否为200

## 常见问题排查

1. 如果看不到数据，检查：
   - 后端服务是否正常运行（访问 `http://localhost:8080/api/product/list`）
   - 前端控制台是否有错误信息
   - 网络请求是否有跨域错误
   - 数据库中是否有测试数据

2. 数据库连接问题：
   - 确认数据库地址和端口是否正确
   - 检查数据库用户名密码是否正确
   - 确认数据库中表名是否正确（products而不是product）

3. 跨域问题：
   - 后端已通过Spring的配置处理了跨域
   - 确认前端API请求URL配置正确
   - 检查是否有cors相关的错误信息