package com.qxy.service.impl;

import com.qxy.common.exception.AppException;
import com.qxy.common.response.ResponseCode;
import com.qxy.infrastructure.redis.IRedisService;
import com.qxy.model.po.Cart;
import com.qxy.model.po.CartItem;
import com.qxy.repository.CartRepository;
import com.qxy.service.CartService;
import com.qxy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private IRedisService redisService;

    @Override
    @Transactional
    public void createCart(Integer userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cartRepository.createCart(cart);
    }

    @Override
    public Cart getCart(Integer userId) {
        return cartRepository.getCartByUserId(userId);
    }

    @Override
    @Transactional
    public void addItem(Integer userId, Integer productId, Integer quantity) {
        Cart cart = getCart(userId);
        if (cart == null) {
            createCart(userId);
            cart = getCart(userId);
        }

        // Check if item already exists in cart
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Update existing item quantity
            updateItemQuantity(userId, productId, existingItem.getQuantity() + quantity);
        } else {
            // Add new item
            CartItem cartItem = new CartItem();
            cartItem.setCartId(cart.getCartId());
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            // Get product price
            cartItem.setPrice(productService.getProductById(productId).getPrice());
            cart.getCartItems().add(cartItem);
            cartRepository.updateCart(cart);
        }
    }

    @Override
    @Transactional
    public void removeItem(Integer userId, Integer productId) {
        Cart cart = getCart(userId);
        if (cart == null) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "Cart not found");
        }

        cart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
        cartRepository.updateCart(cart);
    }

    @Override
    @Transactional
    public void updateItemQuantity(Integer userId, Integer productId, Integer quantity) {
        Cart cart = getCart(userId);
        if (cart == null) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "Cart not found");
        }

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new AppException(ResponseCode.UN_ERROR.getCode(), "Item not found in cart"));

        cartItem.setQuantity(quantity);
        cartRepository.updateCart(cart);
    }

    @Override
    @Transactional
    public void clearCart(Integer userId) {
        Cart cart = getCart(userId);
        if (cart != null) {
            cartRepository.deleteCart(cart.getCartId());
        }
    }
}