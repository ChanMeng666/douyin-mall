package com.qxy.test.dao;

import com.qxy.dao.CartDao;
import com.qxy.model.po.Cart;
import com.qxy.test.config.TestRedisConfiguration;
import com.qxy.test.config.TestPictureConfiguration;
import com.qxy.test.config.TestProductConfiguration;
import com.qxy.test.config.TestAIConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Import({
    TestRedisConfiguration.class,
    TestPictureConfiguration.class,
    TestProductConfiguration.class,
    TestAIConfiguration.class
})
@ActiveProfiles("test")
public class CartDaoTest {

    @Resource
    private CartDao cartDao;

    private static final Integer TEST_USER_ID = 10000;

    @Test
    @Transactional
    public void testCreateCart() {
        Cart cart = new Cart();
        cart.setUserId(TEST_USER_ID);
        cart.setCreatedAt(new Date());
        cart.setUpdatedAt(new Date());

        cartDao.createCart(cart);
        Assert.assertNotNull("Cart ID should be generated", cart.getCartId());

        Cart savedCart = cartDao.getCartByUserId(TEST_USER_ID);
        Assert.assertNotNull("Cart should be found", savedCart);
        Assert.assertEquals("User ID should match", TEST_USER_ID, savedCart.getUserId());
    }

    @Test
    @Transactional
    public void testGetCartByUserId() {
        // First create a cart
        Cart cart = new Cart();
        cart.setUserId(TEST_USER_ID);
        cart.setCreatedAt(new Date());
        cart.setUpdatedAt(new Date());
        cartDao.createCart(cart);

        // Then retrieve it
        Cart foundCart = cartDao.getCartByUserId(TEST_USER_ID);
        Assert.assertNotNull("Cart should be found", foundCart);
        Assert.assertEquals("User ID should match", TEST_USER_ID, foundCart.getUserId());
    }

    @Test
    @Transactional
    public void testDeleteCart() {
        // First create a cart
        Cart cart = new Cart();
        cart.setUserId(TEST_USER_ID);
        cart.setCreatedAt(new Date());
        cart.setUpdatedAt(new Date());
        cartDao.createCart(cart);
        
        // Verify cart was created and has an ID
        Assert.assertNotNull("Cart ID should be generated", cart.getCartId());
        
        // Then delete it
        cartDao.deleteCart(cart.getCartId());

        // Verify deletion
        Cart deletedCart = cartDao.getCartByUserId(TEST_USER_ID);
        Assert.assertNull("Cart should be deleted", deletedCart);
    }
} 