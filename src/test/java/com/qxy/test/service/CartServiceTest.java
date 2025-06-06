package com.qxy.test.service;

import com.qxy.common.exception.AppException;
import com.qxy.controller.dto.cart.CartDTO;
import com.qxy.controller.dto.cart.CartItemDTO;
import com.qxy.service.CartService;
import com.qxy.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    private static final Integer TEST_USER_ID = 10000;
    private static final Integer TEST_PRODUCT_ID = 1; // iPhone 15
    private static final BigDecimal PRODUCT_PRICE = new BigDecimal("999.99");

    @Before
    public void setup() {
        try {
            cleanupTestData();
            // 确保测试商品的库存缓存存在
            productService.storeProductStock(TEST_PRODUCT_ID);
            productService.storeProductStock(TEST_PRODUCT_ID + 1);
            
            // 验证数据库连接
            CartDTO cart = cartService.getCart(TEST_USER_ID);
            log.info("Database connection verified");
        } catch (Exception e) {
            log.error("Failed to setup test environment: ", e);
            throw new RuntimeException("Failed to setup test environment", e);
        }
    }

    @Test
    @Transactional
    public void testCreateCart() {
        try {
            // 创建新购物车
            cartService.createCart(TEST_USER_ID);

            // 验证购物车创建
            CartDTO cart = cartService.getCart(TEST_USER_ID);

            // 断言验证
            Assert.assertNotNull("Cart should not be null", cart);
            Assert.assertEquals("Cart should belong to test user", TEST_USER_ID, cart.getUserId());
            Assert.assertNotNull("Cart ID should be generated", cart.getCartId());
            Assert.assertNotNull("Creation time should be set", cart.getCreatedAt());

            // 验证购物车为空
            Assert.assertTrue("New cart should have no items",
                    cart.getCartItems() == null || cart.getCartItems().isEmpty());

            log.info("Successfully created and verified cart {} for user {}",
                    cart.getCartId(), TEST_USER_ID);
        } catch (Exception e) {
            log.error("Failed to create cart: ", e);
            throw e;
        }
    }

    @Test
    @Transactional
    public void testAddItemToCart() {
        try {
            // 1. 创建购物车
            cartService.createCart(TEST_USER_ID);

            // 2. 添加商品到购物车
            cartService.addItem(TEST_USER_ID, TEST_PRODUCT_ID, 1);

            // 3. 获取购物车验证
            CartDTO cart = cartService.getCart(TEST_USER_ID);
            Assert.assertNotNull("Cart should not be null", cart);

            List<CartItemDTO> items = cart.getCartItems();
            Assert.assertNotNull("Cart items should not be null", items);
            Assert.assertEquals("Should have one item", 1, items.size());

            CartItemDTO item = items.get(0);
            Assert.assertEquals("Product ID should match", TEST_PRODUCT_ID, item.getProductId());
            Assert.assertEquals("Quantity should be 1", 1, item.getQuantity().intValue());
            Assert.assertEquals("Total price should match", PRODUCT_PRICE, item.getTotalPrice());

            log.info("Successfully added item to cart for user {}", TEST_USER_ID);
        } catch (Exception e) {
            log.error("Failed to add item to cart: ", e);
            throw e;
        }
    }

    @Test
    @Transactional
    public void testUpdateCartItemQuantity() {
        try {
            // 1. 创建购物车和添加商品
            cartService.createCart(TEST_USER_ID);
            cartService.addItem(TEST_USER_ID, TEST_PRODUCT_ID, 1);

            // 2. 获取购物车和购物车项
            CartDTO cart = cartService.getCart(TEST_USER_ID);
            CartItemDTO cartItem = cart.getCartItems().get(0);

            // 3. 更新数量
            int newQuantity = 2;
            cartService.updateItemQuantity(cartItem.getCartItemId(), newQuantity);

            // 4. 验证更新结果
            cart = cartService.getCart(TEST_USER_ID);
            CartItemDTO updatedItem = cart.getCartItems().get(0);

            Assert.assertEquals("Quantity should be updated", newQuantity, updatedItem.getQuantity().intValue());
            BigDecimal expectedTotal = PRODUCT_PRICE.multiply(new BigDecimal(newQuantity));
            Assert.assertEquals("Total price should be updated", expectedTotal, updatedItem.getTotalPrice());

            log.info("Successfully updated cart item quantity for user {}", TEST_USER_ID);
        } catch (Exception e) {
            log.error("Failed to update cart item quantity: ", e);
            throw e;
        }
    }

    @Test
    @Transactional
    public void testRemoveCartItem() {
        try {
            // 1. 准备购物车和商品
            cartService.createCart(TEST_USER_ID);
            cartService.addItem(TEST_USER_ID, TEST_PRODUCT_ID, 1);

            // 2. 获取购物车项ID
            CartDTO cart = cartService.getCart(TEST_USER_ID);
            Integer cartItemId = cart.getCartItems().get(0).getCartItemId();

            // 3. 删除商品
            cartService.removeItem(cartItemId);

            // 4. 验证删除结果
            cart = cartService.getCart(TEST_USER_ID);
            Assert.assertTrue("Cart should be empty",
                    cart.getCartItems() == null || cart.getCartItems().isEmpty());

            log.info("Successfully removed item from cart for user {}", TEST_USER_ID);
        } catch (Exception e) {
            log.error("Failed to remove cart item: ", e);
            throw e;
        }
    }

    @Test
    @Transactional
    public void testAddMultipleItemsToCart() {
        try {
            // 1. 创建购物车
            cartService.createCart(TEST_USER_ID);

            // 2. 添加多个商品
            cartService.addItem(TEST_USER_ID, TEST_PRODUCT_ID, 2);      // iPhone 15: 999.99 * 2
            cartService.addItem(TEST_USER_ID, TEST_PRODUCT_ID + 1, 1);  // MacBook Pro: 1999.99 * 1

            // 3. 验证购物车
            CartDTO cart = cartService.getCart(TEST_USER_ID);
            Assert.assertNotNull("Cart should not be null", cart);
            Assert.assertEquals("Should have two items", 2, cart.getCartItems().size());

            // 验证总金额计算
            BigDecimal expectedTotal = PRODUCT_PRICE.multiply(new BigDecimal("2"))
                    .add(new BigDecimal("1999.99")); // 第二个商品的价格
            
            BigDecimal actualTotal = cart.getCartItems().stream()
                    .map(CartItemDTO::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
            Assert.assertEquals("Total price should match", 
                expectedTotal.setScale(2, RoundingMode.HALF_UP),
                actualTotal.setScale(2, RoundingMode.HALF_UP));

            log.info("Successfully tested adding multiple items to cart");
        } catch (Exception e) {
            log.error("Failed to test multiple items: ", e);
            throw e;
        }
    }

    @Test(expected = AppException.class)
    @Transactional
    public void testUpdateQuantityWithInvalidCartItem() {
        cartService.updateItemQuantity(999999, 1);
    }

    @Test(expected = AppException.class)
    @Transactional
    public void testAddItemWithInvalidQuantity() {
        cartService.createCart(TEST_USER_ID);
        cartService.addItem(TEST_USER_ID, TEST_PRODUCT_ID, -1);
    }

    private void cleanupTestData() {
        try {
            CartDTO existingCart = cartService.getCart(TEST_USER_ID);
            if (existingCart != null) {
                cartService.deleteCart(existingCart.getCartId());
                log.info("Cleaned up test data for user {}", TEST_USER_ID);
            }
        } catch (Exception e) {
            log.warn("Error during data cleanup", e);
        }
    }

    @After
    public void cleanup() {
        try {
            cleanupTestData();
        } catch (Exception e) {
            log.warn("Error during cleanup", e);
        }
    }
}