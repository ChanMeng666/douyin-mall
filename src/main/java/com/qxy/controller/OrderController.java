package com.qxy.controller;

import com.qxy.common.response.Response;
import com.qxy.common.response.ResponseCode;
import com.qxy.controller.dto.order.CreateOrderRequestDto;
import com.qxy.controller.dto.order.CreateOrderResponseDto;
import com.qxy.model.req.CreateOrderReq;
import com.qxy.model.res.CreateOrderRes;
import com.qxy.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: dawang
 * @Description: TODO
 * @Date: 2025/2/1 23:32
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {
    @Resource
    private IOrderService orderService;

    /**
     * 创建订单
     * @param createOrderDto
     * @return
     */
    @RequestMapping("/create")
    public Response<CreateOrderResponseDto> createOrder(CreateOrderRequestDto createOrderDto) {
        try {
            if(null == createOrderDto) {
                return Response.<CreateOrderResponseDto>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .data(null)
                        .build();
            }
            log.info("创建订单请求: userId:{} , cartId:{} , payType:{}", createOrderDto.getUserId(),createOrderDto.getCartId(), createOrderDto.getPayType());
            CreateOrderReq createOrderReq = new CreateOrderReq();
            createOrderReq.setUserId(createOrderDto.getUserId());
            createOrderReq.setCartId(createOrderDto.getCartId());
            createOrderReq.setPayType(createOrderDto.getPayType());
            CreateOrderRes order = orderService.createOrder(createOrderReq);
            if (order == null) {
                return Response.<CreateOrderResponseDto>builder()
                        .code(ResponseCode.UN_ERROR.getCode())
                        .info(ResponseCode.UN_ERROR.getInfo())
                        .data(null)
                        .build();
            }
            log.info("创建订单成功: orderId:{} , totalAmount:{} , status:{} , payType:{}", order.getOrderId(), order.getTotalAmount(), order.getStatus(), order.getPayType());
            return Response.<CreateOrderResponseDto>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(CreateOrderResponseDto.builder()
                            .orderId(order.getOrderId())
                            .totalAmount(order.getTotalAmount())
                            .status(order.getStatus())
                            .payType(order.getPayType())
                            .createdAt(order.getCreatedAt())
                            .build())
                    .build();
        }catch (Exception e) {
            log.error("创建订单失败:  userId:{},e:{}", createOrderDto.getUserId(),e.getMessage());
            return Response.<CreateOrderResponseDto>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }

    }
}
