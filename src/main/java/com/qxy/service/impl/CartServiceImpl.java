package com.qxy.service.impl;

import com.qxy.common.exception.AppException;
import com.qxy.common.response.ResponseCode;
import com.qxy.controller.dto.cart.CartDTO;
import com.qxy.controller.dto.cart.CartItemDTO;
import com.qxy.model.po.Cart;
import com.qxy.model.po.CartItem;
import com.qxy.dao.CartItemDao;
import com.qxy.dao.CartDao;
import com.qxy.service.CartService;
import com.qxy.service.ProductService;
import com.qxy.model.res.ProductRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDao cartRepository;

    @Autowired
    private CartItemDao cartItemRepository;

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
    public CartDTO getCart(Integer userId) {
        Cart cart = cartRepository.getCartByUserId(userId);
        if (cart == null) {
            return null;
        }
        cart.setCartItems(cartItemRepository.getItemsByCartId(cart.getCartId()));
        return convertToDTO(cart);
    }

    @Override
    @Transactional
    public void addItem(Integer userId, Integer productId, Integer quantity) {
        if (quantity <= 0) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "Invalid quantity");
        }

        Cart cart = cartRepository.getCartByUserId(userId);
        if (cart == null) {
            createCart(userId);
            cart = cartRepository.getCartByUserId(userId);
        }

        ProductRes product = productService.getProductById(productId);
        if (product == null) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "Product not found");
        }

        CartItem existingItem = cartItemRepository.getItemByProductId(cart.getCartId(), productId);
        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;
            existingItem.setQuantity(newQuantity);
            existingItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(newQuantity)));
            cartItemRepository.updateItem(existingItem);
        } else {
            CartItem newItem = CartItem.builder()
                .cartId(cart.getCartId())
                .productId(productId)
                .quantity(quantity)
                .totalPrice(product.getPrice().multiply(new BigDecimal(quantity)))
                .build();
            cartItemRepository.insertItem(newItem);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeItem(Integer cartItemId) {
        try {
            cartItemRepository.deleteItem(cartItemId);
        } catch (Exception e) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "Failed to remove cart item");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateItemQuantity(Integer cartItemId, Integer quantity) {
        if (quantity <= 0) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "Invalid quantity");
        }

        try {
            List<CartItem> items = cartItemRepository.getItemsByCartId(cartItemId);
            if (items == null || items.isEmpty()) {
                throw new AppException(ResponseCode.UN_ERROR.getCode(), "Cart item not found");
            }

            CartItem item = items.get(0);
            ProductRes product = productService.getProductById(item.getProductId());
            if (product == null) {
                throw new AppException(ResponseCode.UN_ERROR.getCode(), "Product not found");
            }

            item.setQuantity(quantity);
            item.setTotalPrice(product.getPrice().multiply(new BigDecimal(quantity)));
            cartItemRepository.updateItem(item);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "Failed to update cart item");
        }
    }

    @Override
    @Transactional
    public void deleteCart(Integer cartId) {
        cartRepository.deleteCart(cartId);
    }

    private CartDTO convertToDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getCartId());
        dto.setUserId(cart.getUserId());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());

        if (cart.getCartItems() != null) {
            dto.setCartItems(cart.getCartItems().stream()
                    .map(this::convertToCartItemDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private CartItemDTO convertToCartItemDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemId(item.getCartItemId());
        dto.setCartId(item.getCartId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        dto.setTotalPrice(item.getTotalPrice());
        dto.setCreateAt(item.getCreateAt());
        dto.setUpdateAt(item.getUpdateAt());
        return dto;
    }
}