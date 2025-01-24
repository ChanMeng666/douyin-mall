package com.qxy.controller;

import com.qxy.common.response.Response;
import com.qxy.common.response.ResponseCode;
import com.qxy.model.po.Cart;
import com.qxy.model.req.AddToCartRequest;
import com.qxy.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public Response<Void> addToCart(@RequestBody AddToCartRequest request) {
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
    public Response<Cart> getCart(@PathVariable Integer userId) {
        Cart cart = cartService.getCart(userId);
        return Response.<Cart>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(cart)
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
}