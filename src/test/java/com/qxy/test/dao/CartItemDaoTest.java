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

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartItemDaoTest {

    @Resource
    private CartItemDao cartItemDao;

    @Resource
    private CartDao cartDao;

    private static final Integer TEST_USER_ID = 10000;
    private static final Integer TEST_PRODUCT_ID = 1;
    private Integer testCartId;

    @Before
    @Transactional
    public void setup() {
        Cart cart = new Cart();
        cart.setUserId(TEST_USER_ID);
        cart.setCreatedAt(new Date());
        cart.setUpdatedAt(new Date());
        cartDao.createCart(cart);
        testCartId = cart.getCartId();
    }

    @Test
    @Transactional
    public void testInsertItem() {
        CartItem item = createTestCartItem();
        cartItemDao.insertItem(item);
        Assert.assertNotNull("Cart item ID should be generated", item.getCartItemId());

        List<CartItem> items = cartItemDao.getItemsByCartId(testCartId);
        Assert.assertEquals("Should have one item", 1, items.size());
        Assert.assertEquals("Product ID should match", TEST_PRODUCT_ID, items.get(0).getProductId());
    }

    @Test
    @Transactional
    public void testDeleteItem() {
        CartItem item = createTestCartItem();
        cartItemDao.insertItem(item);

        cartItemDao.deleteItem(item.getCartItemId());

        List<CartItem> items = cartItemDao.getItemsByCartId(testCartId);
        Assert.assertTrue("Cart should be empty", items.isEmpty());
    }

    @Test
    @Transactional
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
    @Transactional
    public void testGetItemsByCartId() {
        CartItem item1 = createTestCartItem();
        CartItem item2 = createTestCartItem();
        item2.setProductId(TEST_PRODUCT_ID + 1);
        
        cartItemDao.insertItem(item1);
        cartItemDao.insertItem(item2);

        List<CartItem> items = cartItemDao.getItemsByCartId(testCartId);
        Assert.assertEquals("Should have two items", 2, items.size());
    }

    @Test
    @Transactional
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