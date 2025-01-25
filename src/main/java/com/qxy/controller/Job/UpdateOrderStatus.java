package com.qxy.controller.Job;

import com.qxy.model.po.Order;
import com.qxy.service.IOrderService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: dawang
 * @Description: 每10秒查询超时订单，并修改超时订单状态
 * @Date: 2025/1/25 23:03
 * @Version: 1.0
 */
@Slf4j
@Component
public class UpdateOrderStatus {
    @Resource
    private  IOrderService orderService;
    @Scheduled(cron = "0/10 * * * * ?")
    public void updateOrderStatus() {
        try{
            log.info("定时任务: 更新超时订单");
            List<Integer> OvertimeOrdersId = orderService.getOvertimeOrders();
            if(null == OvertimeOrdersId && OvertimeOrdersId.size() == 0) {
                log.info("定时任务，超时30分钟订单取消，暂无超时未支付订单 orderIds is null");
                return;
            }
            for(int orderId : OvertimeOrdersId) {
                orderService.updateOrderStatusToCancelled(orderId);
                log.info("定时任务，超时30分钟订单取消，orderId is {}" , orderId);
            }
        }catch (Exception e) {
            log.error("定时任务，超时30分钟订单取消，发生异常", e);
        }

    }
}
