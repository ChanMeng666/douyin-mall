package com.qxy.controller;

import com.qxy.common.response.Response;
import com.qxy.common.response.ResponseCode;
import com.qxy.controller.dto.cart.*;
import com.qxy.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public Response<Void> addToCart(@RequestBody AddToCartRequestDTO request) {
        cartService.addItem(request.getUserId(), request.getProductId(), request.getQuantity());
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @DeleteMapping("/items/{cartItemId}")
    public Response<Void> removeCartItem(@PathVariable Integer cartItemId) {
        cartService.removeItem(cartItemId);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @GetMapping("/user/{userId}")
    public Response<CartResponseDTO> getCart(@PathVariable Integer userId) {
        CartDTO cartDTO = cartService.getCart(userId);
        CartResponseDTO response = convertToResponse(cartDTO);
        return Response.<CartResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(response)
                .build();
    }

    @PutMapping("/items/{cartItemId}")
    public Response<Void> updateCartItemQuantity(
            @PathVariable Integer cartItemId,
            @RequestParam Integer quantity) {
        cartService.updateItemQuantity(cartItemId, quantity);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @DeleteMapping("/{cartId}")
    public Response<Void> deleteCart(@PathVariable Integer cartId) {
        cartService.deleteCart(cartId);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    private CartResponseDTO convertToResponse(CartDTO cartDTO) {
        CartResponseDTO response = new CartResponseDTO();
        response.setCartId(cartDTO.getCartId());
        response.setUserId(cartDTO.getUserId());
        response.setItems(cartDTO.getCartItems());
        // Calculate total amount
        BigDecimal total = cartDTO.getCartItems().stream()
                .map(CartItemDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotalAmount(total);
        return response;
    }
}