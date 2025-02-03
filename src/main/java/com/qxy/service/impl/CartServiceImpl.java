//package com.qxy.service.impl;
//
//import com.qxy.common.exception.AppException;
//import com.qxy.common.response.ResponseCode;
//import com.qxy.model.po.Cart;
//import com.qxy.model.po.CartItem;
//import com.qxy.dao.CartItemDao;
//import com.qxy.dao.CartDao;
//import com.qxy.service.CartService;
//import com.qxy.service.ProductService;
//import com.qxy.service.dto.ProductDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//
//@Service
//public class CartServiceImpl implements CartService {
//
//    @Autowired
//    private CartDao cartRepository;
//
//    @Autowired
//    private CartItemDao cartItemRepository;
//
//    @Autowired
//    private ProductService productService;
//
//    @Override
//    @Transactional
//    public void createCart(Integer userId) {
//        Cart cart = new Cart();
//        cart.setUserId(userId);
//        cartRepository.createCart(cart);
//    }
//
//    @Override
//    public Cart getCart(Integer userId) {
//        Cart cart = cartRepository.getCartByUserId(userId);
//        if (cart == null) {
//            return null;
//        }
//        cart.setCartItems(cartItemRepository.getItemsByCartId(cart.getCartId()));
//        return cart;
//    }
//
//    @Override
//    @Transactional
//    public void addItem(Integer userId, Integer productId, Integer quantity) {
//        Cart cart = getCart(userId);
//        if (cart == null) {
//            createCart(userId);
//            cart = getCart(userId);
//        }
//
//        // Verify product exists and get price
//        ProductDTO product = productService.getProductById(productId);
//        if (product == null) {
//            throw new AppException(ResponseCode.UN_ERROR.getCode(), "Product not found");
//        }
//
//        CartItem existingItem = cartItemRepository.getItemByProductId(cart.getCartId(), productId);
//        if (existingItem != null) {
//            existingItem.setQuantity(existingItem.getQuantity() + quantity);
//            existingItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(existingItem.getQuantity())));
//            cartItemRepository.updateItem(existingItem);
//        } else {
//            CartItem newItem = new CartItem();
//            newItem.setCartId(cart.getCartId());
//            newItem.setProductId(productId);
//            newItem.setQuantity(quantity);
//            newItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(quantity)));
//            cartItemRepository.insertItem(newItem);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void removeItem(Integer cartItemId) {
//        cartItemRepository.deleteItem(cartItemId);
//    }
//
//    @Override
//    @Transactional
//    public void updateItemQuantity(Integer cartItemId, Integer quantity) {
//        CartItem item = cartItemRepository.getItemsByCartId(cartItemId)
//                .stream()
//                .findFirst()
//                .orElseThrow(() -> new AppException(ResponseCode.UN_ERROR.getCode(), "Cart item not found"));
//
//        ProductDTO product = productService.getProductById(item.getProductId());
//        item.setQuantity(quantity);
//        item.setTotalPrice(product.getPrice().multiply(new BigDecimal(quantity)));
//        cartItemRepository.updateItem(item);
//    }
//
//    @Override
//    @Transactional
//    public void deleteCart(Integer cartId) {
//        cartRepository.deleteCart(cartId);
//    }
//}



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
import com.qxy.service.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        Cart cart = cartRepository.getCartByUserId(userId);
        if (cart == null) {
            createCart(userId);
            cart = cartRepository.getCartByUserId(userId);
        }

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

    private CartDTO convertToDTO(Cart cart) {
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