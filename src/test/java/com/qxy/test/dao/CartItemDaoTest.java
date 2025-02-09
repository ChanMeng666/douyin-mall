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
import java.util.Date;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
    "spring.task.scheduling.enabled=false",
    "spring.scheduling.enabled=false",
    "spring.task.execution.enabled=false",
    "spring.task.execution.pool.core-size=0",
    "spring.task.execution.pool.max-size=0",
    "spring.task.execution.pool.queue-capacity=0"
})
@ActiveProfiles("test")
@Import({TestRedisConfiguration.class, TestConfig.class})
@Transactional(
    isolation = Isolation.READ_COMMITTED,
    propagation = Propagation.REQUIRED,
    timeout = 10
)
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
    public void testInsertItem() {
        CartItem item = createTestCartItem();
        log.info("Inserting cart item: {}", item);
        cartItemDao.insertItem(item);
        log.info("Generated ID: {}", item.getCartItemId());
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
    public void testUpdateItem() {
        CartItem item = createTestCartItem();
        cartItemDao.insertItem(item);

        // Update quantity and price
        item.setQuantity(2);
        item.setTotalPrice(new BigDecimal("1999.98"));
        cartItemDao.updateItem(item);

        List<CartItem> items = cartItemDao.getItemsByCartId(testCartId);
        Assert.assertEquals("Quantity should be updated", 2, items.get(0).getQuantity().intValue());
        Assert.assertEquals("Total price should be updated", 
            new BigDecimal("1999.98"), items.get(0).getTotalPrice());
    }

    @Test
    public void testGetItemsByCartId() {
        // 先插入测试数据
        CartItem item1 = createTestCartItem();
        CartItem item2 = createTestCartItem();
        item2.setProductId(TEST_PRODUCT_ID + 1);
        
        log.info("Inserting first item: {}", item1);
        cartItemDao.insertItem(item1);
        log.info("First item generated ID: {}", item1.getCartItemId());
        Assert.assertNotNull("Item1 ID should be generated", item1.getCartItemId());
        
        log.info("Inserting second item: {}", item2);
        cartItemDao.insertItem(item2);
        log.info("Second item generated ID: {}", item2.getCartItemId());
        Assert.assertNotNull("Item2 ID should be generated", item2.getCartItemId());

        // 验证查询结果
        List<CartItem> items = cartItemDao.getItemsByCartId(testCartId);
        log.info("Found items: {}", items);
        Assert.assertNotNull("Items list should not be null", items);
        Assert.assertEquals("Should have two items", 2, items.size());
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