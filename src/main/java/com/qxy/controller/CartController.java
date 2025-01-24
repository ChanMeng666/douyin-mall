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

    @GetMapping("/get")
    public Response<Cart> getCart(@RequestParam Integer userId) {
        return Response.<Cart>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(cartService.getCart(userId))
                .build();
    }

    @PostMapping("/update")
    public Response<Void> updateCartItem(@RequestParam Integer userId,
                                         @RequestParam Integer productId,
                                         @RequestParam Integer quantity) {
        cartService.updateItemQuantity(userId, productId, quantity);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @DeleteMapping("/remove")
    public Response<Void> removeFromCart(@RequestParam Integer userId,
                                         @RequestParam Integer productId) {
        cartService.removeItem(userId, productId);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    @DeleteMapping("/clear")
    public Response<Void> clearCart(@RequestParam Integer userId) {
        cartService.clearCart(userId);
        return Response.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build();
    }
}