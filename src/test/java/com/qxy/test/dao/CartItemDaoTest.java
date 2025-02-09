package com.qxy.test.dao;

import com.qxy.dao.CartDao;
import com.qxy.dao.CartItemDao;
import com.qxy.model.po.Cart;
import com.qxy.model.po.CartItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import com.qxy.test.config.TestRedisConfiguration;
import com.qxy.test.config.TestConfig;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
    "spring.task.scheduling.enabled=false",
    "spring.scheduling.enabled=false",
    "spring.task.execution.enabled=false",
    "spring.main.allow-bean-definition-overriding=true"
})
@Import({TestRedisConfiguration.class, TestConfig.class})
@ActiveProfiles("test")
public class CartItemDaoTest {

    @Resource
    private CartItemDao cartItemDao;

    @Resource
    private CartDao cartDao;

    private static final Integer TEST_USER_ID = 10000;
    private static final Integer TEST_PRODUCT_ID = 1;
    private Integer testCartId;

    @Before
    public void setup() {
        Cart cart = new Cart();
        cart.setUserId(TEST_USER_ID);
        cart.setCreatedAt(new Date());
        cart.setUpdatedAt(new Date());
        cartDao.createCart(cart);
        testCartId = cart.getCartId();
        Assert.assertNotNull("Cart ID should be generated", testCartId);
    }

    @Test
    @Transactional
    public void testInsertItem() {
        CartItem item = createTestCartItem();
        cartItemDao.insertItem(item);
        Assert.assertNotNull("Cart item ID should be generated", item.getCartItemId());
    }

    @Test
    public void testDeleteItem() {
        // 创建并插入购物车项
        CartItem item = createTestCartItem();
        cartItemDao.insertItem(item);

        // 确认插入成功
        CartItem insertedItem = cartItemDao.getItemByProductId(testCartId, TEST_PRODUCT_ID);
        Assert.assertNotNull("Cart item should be inserted", insertedItem);
        Assert.assertNotNull("Cart item ID should be generated", insertedItem.getCartItemId());

        // 删除购物车项
        cartItemDao.deleteItem(insertedItem.getCartItemId());

        // 验证删除成功
        List<CartItem> items = cartItemDao.getItemsByCartId(testCartId);
        Assert.assertTrue("Cart should be empty", items == null || items.isEmpty());
    }

    @Test
    @Transactional
    public void testUpdateItem() {
        // 先插入一个购物车项
        CartItem item = createTestCartItem();
        cartItemDao.insertItem(item);
        Assert.assertNotNull(item.getCartItemId());

        // 更新数量和总价
        item.setQuantity(2);
        item.setTotalPrice(new BigDecimal("1999.98"));
        item.setUpdateAt(new Date());
        cartItemDao.updateItem(item);

        // 验证更新结果
        CartItem updatedItem = cartItemDao.getItemByProductId(testCartId, TEST_PRODUCT_ID);
        Assert.assertNotNull("Updated item should exist", updatedItem);
        Assert.assertEquals("Quantity should be updated", 2, updatedItem.getQuantity().intValue());
        Assert.assertEquals("Total price should be updated", 
            new BigDecimal("1999.98").setScale(2, RoundingMode.HALF_UP), 
            updatedItem.getTotalPrice().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    @Transactional
    public void testGetItemsByCartId() {
        try {
            // 插入两个测试项
            CartItem item1 = createTestCartItem();
            cartItemDao.insertItem(item1);
            Assert.assertNotNull("Item1 ID should be generated", item1.getCartItemId());
            
            CartItem item2 = createTestCartItem();
            item2.setProductId(TEST_PRODUCT_ID + 1);
            cartItemDao.insertItem(item2);
            Assert.assertNotNull("Item2 ID should be generated", item2.getCartItemId());

            List<CartItem> items = cartItemDao.getItemsByCartId(testCartId);
            Assert.assertNotNull("Items list should not be null", items);
            Assert.assertEquals("Should have two items", 2, items.size());
        } catch (Exception e) {
            log.error("Test failed with error: ", e);
            throw e;
        }
    }

    @Test
    public void testGetItemByProductId() {
        CartItem item = createTestCartItem();
        cartItemDao.insertItem(item);

        CartItem foundItem = cartItemDao.getItemByProductId(testCartId, TEST_PRODUCT_ID);
        Assert.assertNotNull("Should find the item", foundItem);
        Assert.assertEquals("Product ID should match", TEST_PRODUCT_ID, foundItem.getProductId());
    }

    private CartItem createTestCartItem() {
        CartItem item = new CartItem();
        item.setCartId(testCartId);
        item.setProductId(TEST_PRODUCT_ID);
        item.setQuantity(1);
        item.setTotalPrice(new BigDecimal("999.99"));
        item.setCreateAt(new Date());
        item.setUpdateAt(new Date());
        return item;
    }
} 