package com.qxy.controller.Job;

import com.qxy.dao.OrderItemsDao;
import com.qxy.dao.dataobject.ProductDO;
import com.qxy.dao.ProductDao;
import com.qxy.service.IOrderService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private OrderItemsDao orderItemsDao;

    @Resource
    private ProductDao productDao;
    @Scheduled(cron = "0/10 * * * * ?")
    public void updateOrderStatus() {
        try{
            log.info("定时任务: 更新超时订单");
            List<Integer> OvertimeOrdersId = orderService.getOvertimeOrders();
            if(null == OvertimeOrdersId && OvertimeOrdersId.isEmpty()) {
                log.info("定时任务，超时30分钟订单取消，暂无超时未支付订单 orderIds is null");
                return;
            }
            // 超时订单回滚库存表
            Map<Integer, Integer> rollbackMap = new HashMap<>();
            for(int orderId : OvertimeOrdersId) {
                orderService.updateOrderStatusToCancelled(orderId);
                //根据订单号获取订单商品信息
                List<ProductDO> productDOs = orderItemsDao.queryOrderItemByOrderId(orderId);
                for(ProductDO productDO : productDOs) {
                    rollbackMap.put(productDO.getProductId(), productDO.getStock());
                    //回滚数据库库存
                    productDao.reduceProductStock(productDO.getProductId(), -productDO.getStock());
                }
                // 回滚缓存库存
                orderService.rollbackStock(rollbackMap);

                log.info("定时任务，超时30分钟订单取消，orderId is {}" , orderId);

            }
        }catch (Exception e) {
            log.error("定时任务，超时30分钟订单取消，发生异常", e);
        }

    }
}
