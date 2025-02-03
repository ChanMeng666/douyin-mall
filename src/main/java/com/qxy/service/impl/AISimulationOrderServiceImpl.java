package com.qxy.service.impl;

import com.qxy.common.exception.BusinessException;
import com.qxy.controller.dto.ai.AiOrderRequestDTO;
import com.qxy.controller.dto.ai.AiOrderResponseDTO;
import com.qxy.dao.AiOrderDao;
import com.qxy.dao.AiProductDao;
import com.qxy.model.po.AiOrder;
import com.qxy.model.po.Product;
import com.qxy.service.AISimulationOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author water
 * @description: TODO
 * @date 2025/2/3 20:15
 * @version 1.0
 */
@Service
public class AISimulationOrderServiceImpl implements AISimulationOrderService {
    @Autowired
    private AiProductDao aiProductMapper;
    @Autowired
    private AiOrderDao aiOrderMapper;

    @Override
    @Transactional
    public AiOrderResponseDTO createOrderFromMessage(AiOrderRequestDTO requestDTO) {
        // 1. 解析用户消息中的商品关键词
        String keyword = extractKeyword(requestDTO.getMessage());
        Product product = aiProductMapper.searchProductsByName(keyword)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException("商品不存在或已下架"));

        // 2. 检查库存
        if (product.getStock() < 1) {
            throw new BusinessException("库存不足");
        }

        // 3. 创建订单
        AiOrder order = new AiOrder();
        order.setUserId(requestDTO.getUserId());
        order.setTotalAmount(product.getPrice());
        order.setStatus("pay_wait");
        aiOrderMapper.insertOrder(order);

        // 4. 扣减库存
        aiProductMapper.decreaseStock(product.getProductId(), 1);

        // 5. 返回响应
        AiOrderResponseDTO response = new AiOrderResponseDTO();
        response.setOrderId(String.valueOf(order.getOrderId()));
        response.setPaymentUrl(generatePaymentUrl(order.getOrderId()));
        response.setStatus("pending");
        return response;
    }

    // 提取关键词（示例：从“我想要买手机”中提取“手机”）
    private String extractKeyword(String message) {
        return message.replace("我想要买", "").trim();
    }

    // 生成支付链接（伪代码）
    private String generatePaymentUrl(Integer orderId) {
        return "https://payment.example.com/pay?orderId=" + orderId;
    }
}