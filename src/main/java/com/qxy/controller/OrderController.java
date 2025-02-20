package com.qxy.controller;

import com.qxy.common.exception.AppException;
import com.qxy.common.response.Response;
import com.qxy.common.response.ResponseCode;
import com.qxy.controller.dto.order.*;
import com.qxy.model.po.CartItem;
import com.qxy.model.req.CreateOrderReq;
import com.qxy.model.req.QueryHistoryOrderReq;
import com.qxy.model.req.CancelOrderReq;
import com.qxy.model.res.CreateOrderRes;
import com.qxy.model.res.QueryHistoryOrderRes;
import com.qxy.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: dawang
 * @Description: 控制类
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
    @RequestMapping(value = "/create" ,method = {RequestMethod.POST})
    public Response<CreateOrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto createOrderDto) {
        try {
            //检查请求参数合法性
            validateCreateRequest(createOrderDto);
            log.info("创建订单请求: userId:{} , cartId:{} , payType:{}", createOrderDto.getUserId(),createOrderDto.getCartId(), createOrderDto.getPayType());

            // 转换请求对象
            CreateOrderReq serviceReq = convertToServiceReq(createOrderDto);

            // 调用服务层创建订单
            CreateOrderRes order = orderService.createOrder(serviceReq);
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
        }catch (AppException e) {
            log.error("创建订单失败:  userId:{},e:{}", createOrderDto.getUserId(),e.getMessage());
            return Response.<CreateOrderResponseDto>builder()
                    .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                    .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                    .data(null)
                    .build();
        }

    }

    /**
     * 查询历史订单
     * @param queryHistoryOrderReqDto
     * @return
     */
    @RequestMapping(value = "/queryHistoryOrder",method = {RequestMethod.GET})
    public Response<QueryHistoryOrderResponseDto> queryHistoryOrderByUserId(@RequestBody QueryHistoryOrderRequestDto queryHistoryOrderReqDto) {
        try {
            if(null == queryHistoryOrderReqDto) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            log.info("查询历史订单请求: userId:{}", queryHistoryOrderReqDto.getUserId());
            QueryHistoryOrderReq queryHistoryOrderReq =  QueryHistoryOrderReq.builder()
                    .userId(queryHistoryOrderReqDto.getUserId())
                    .build();
            QueryHistoryOrderRes response = orderService.queryHistoryOrderByUserId(queryHistoryOrderReq);
            if (response == null) {
                return Response.<QueryHistoryOrderResponseDto>builder()
                        .code(ResponseCode.UN_ERROR.getCode())
                        .info(ResponseCode.UN_ERROR.getInfo())
                        .data(null)
                        .build();
            }
            return Response.<QueryHistoryOrderResponseDto>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(QueryHistoryOrderResponseDto.builder()
                            .userId(response.getUserId())
                            .historyOrders(response.getHistoryOrders())
                            .build())
                    .build();
        }catch (AppException e) {
            assert queryHistoryOrderReqDto != null;
            log.error("查询历史订单失败: userId:{},e:{}", queryHistoryOrderReqDto.getUserId(),e.getMessage());
            return Response.<QueryHistoryOrderResponseDto>builder()
                    .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                    .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                    .data(null)
                    .build();
        }
    }

    /**
     * 取消订单
     * @param cancelOrderRequestDto
     * @return
     */
    @RequestMapping(value = "/cancelOrder",method = {RequestMethod.POST})
    public Response<Boolean> cancelOrder(@RequestBody CancelOrderRequestDto cancelOrderRequestDto) {
        try {
            if(null == cancelOrderRequestDto) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            log.info("取消订单请求: orderId:{}", cancelOrderRequestDto.getOrderId());
            CancelOrderReq cancelOrderReq =  CancelOrderReq.builder()
                    .orderId(cancelOrderRequestDto.getOrderId())
                    .userId(cancelOrderRequestDto.getUserId())
                    .build();
            orderService.updateOrderStatusToCancelled(cancelOrderReq.getOrderId());
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
        }catch (AppException e){
            return Response.<Boolean>builder()
                    .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                    .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                    .data(false)
                    .build();
        }
    }

    private void validateCreateRequest(CreateOrderRequestDto dto) {
        if (dto == null || CollectionUtils.isEmpty(dto.getCartItemDtos())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
    }

    private CreateOrderReq convertToServiceReq(CreateOrderRequestDto dto) {
        return CreateOrderReq.builder()
                .userId(dto.getUserId())
                .cartId(dto.getCartId())
                .payType(dto.getPayType())
                .cartItems(convertCartItems(dto.getCartItemDtos()))
                .build();
    }
    private List<CartItem> convertCartItems(List<CartItemDto> dtos) {
        List<CartItem> cartItems = dtos.stream().map(dto -> CartItem.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .totalPrice(dto.getTotalPrice())
                .build()).collect(Collectors.toList());
       return cartItems;
    }
}
