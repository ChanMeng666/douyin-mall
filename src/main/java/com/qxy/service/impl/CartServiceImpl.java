package com.qxy.service.impl;

import com.qxy.common.exception.AppException;
import com.qxy.common.response.ResponseCode;
import com.qxy.model.po.Cart;
import com.qxy.model.po.CartItem;
import com.qxy.repository.CartItemRepository;
import com.qxy.repository.CartRepository;
import com.qxy.service.CartService;
import com.qxy.service.ProductService;
import com.qxy.service.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public void createCart(Integer userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cartRepository.createCart(cart);
    }

    @Override
    public Cart getCart(Integer userId) {
        Cart cart = cartRepository.getCartByUserId(userId);
        if (cart == null) {
            return null;
        }
        cart.setCartItems(cartItemRepository.getItemsByCartId(cart.getCartId()));
        return cart;
    }

    @Override
    @Transactional
    public void addItem(Integer userId, Integer productId, Integer quantity) {
        Cart cart = getCart(userId);
        if (cart == null) {
            createCart(userId);
            cart = getCart(userId);
        }

        // Verify product exists and get price
        ProductDTO product = productService.getProductById(productId);
        if (product == null) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "Product not found");
        }

        CartItem existingItem = cartItemRepository.getItemByProductId(cart.getCartId(), productId);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(existingItem.getQuantity())));
            cartItemRepository.updateItem(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCartId(cart.getCartId());
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            newItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(quantity)));
            cartItemRepository.insertItem(newItem);
        }
    }

    @Override
    @Transactional
    public void removeItem(Integer cartItemId) {
        cartItemRepository.deleteItem(cartItemId);
    }

    @Override
    @Transactional
    public void updateItemQuantity(Integer cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.getItemsByCartId(cartItemId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new AppException(ResponseCode.UN_ERROR.getCode(), "Cart item not found"));

        ProductDTO product = productService.getProductById(item.getProductId());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(new BigDecimal(quantity)));
        cartItemRepository.updateItem(item);
    }

    @Override
    @Transactional
    public void deleteCart(Integer cartId) {
        cartRepository.deleteCart(cartId);
    }
}