package com.qxy.controller.Job;

import com.qxy.dao.dataobject.ProductDO;
import com.qxy.dao.mapper.ProductDao;
import com.qxy.model.po.CartItem;
import com.qxy.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: dawang
 * @Description: 修改商品数据库库存
 * @Date: 2025/1/31 23:03
 * @Version: 1.0
 */
@Slf4j
@Component
public class UpdateProductStock {
    @Resource
    private IOrderService orderService;
    @Resource
    private ProductDao productDao;
    //每5秒执行一次
    @Scheduled(cron = "0/5 * * * * ?")
    public void updateProductStock() {
        log.info("定时任务:开始修改商品库存");
        try{
            Map<Integer, Integer> stockMap= orderService.takeQueue();
            if(null == stockMap) {
                log.info("定时任务:修改商品库存，暂无商品库存队列");
            }
            stockMap.forEach((productId, quantity) -> {
                productDao.reduceProductStock(productId, quantity);
            });
        }catch (Exception e) {
            log.error("定时任务:修改商品库存，发生异常", e);
        }
    }
}
