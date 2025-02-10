package com.qxy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qxy.common.constant.Constants;
import com.qxy.common.exception.BusinessException;
import com.qxy.controller.dto.ai.AiOrderRequestDTO;
import com.qxy.controller.dto.ai.AiOrderResponseDTO;
import com.qxy.dao.AiOrderDao;
import com.qxy.dao.AiProductDao;
import com.qxy.model.po.AiOrder;
import com.qxy.model.po.OrderItems;
import com.qxy.model.po.Product;
import com.qxy.service.AISimulationOrderService;
import com.qxy.service.QwenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private QwenAIService qwenAIService; // 注入 AI 服务

    @Override
    @Transactional
    public AiOrderResponseDTO createOrderFromMessage(AiOrderRequestDTO requestDTO) {
        try {
            // 1. 调用 AI 解析用户意图
            String aiResponse = qwenAIService.callQwenAI(requestDTO.getMessage());
            Map<String, Object> parsedMessage = parseAiResponse(aiResponse);

            String productName = (String) parsedMessage.get("productName");
            int quantity = (int) parsedMessage.get("quantity");

            // 2. 查询所有符合条件的商品
            List<Product> products = aiProductMapper.searchProductsByName(productName);
            if (products == null || products.isEmpty()) {
                throw new BusinessException("未找到商品：" + productName);
            }

            // 3. 选择价格最低的商品
            Product cheapestProduct = products.stream()
                    .filter(p -> p.getStatus().equals("active")) // 确保商品状态为 active
                    .min(Comparator.comparing(Product::getPrice))
                    .orElseThrow(() -> new BusinessException("没有可用的商品：" + productName));

            // 4. 检查库存是否足够
            if (cheapestProduct.getStock() < quantity) {
                throw new BusinessException("库存不足");
            }

            // 5. 创建订单记录
            AiOrder order = new AiOrder();
            order.setUserId(requestDTO.getUserId());
            order.setTotalAmount(cheapestProduct.getPrice().multiply(BigDecimal.valueOf(quantity)));
            order.setStatus(Constants.OrderStatus.PAY_WAIT.getCode());
            order.setPayType(1); // 默认支付方式为支付宝 (1: alipay)
            aiOrderMapper.insertOrder(order);

            // 6. 创建订单项记录
            OrderItems orderItem = new OrderItems();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setProductId(cheapestProduct.getProductId());
            orderItem.setQuantity(quantity);
            orderItem.setPrice(cheapestProduct.getPrice());
            aiOrderMapper.insertOrderItem(orderItem);

            // 7. 扣减库存
            aiProductMapper.decreaseStock(cheapestProduct.getProductId(), quantity);

            // 8. 返回响应
            AiOrderResponseDTO response = new AiOrderResponseDTO();
            response.setOrderId(String.valueOf(order.getOrderId()));
            response.setPaymentUrl(generatePaymentUrl(order.getOrderId()));
            response.setStatus(Constants.OrderStatus.PAY_WAIT.getDesc());
            return response;
        } catch (Exception e) {
            // 捕获异常并抛出自定义业务异常
            throw new BusinessException("调用 AI 服务失败：" + e.getMessage());
        }
    }

    /**
     * 解析 AI 的响应
     */
    private Map<String, Object> parseAiResponse(String aiResponse) {
        // 假设 AI 返回的响应格式为 JSON，例如：
        // {"productName": "手机", "quantity": 2}
        JSONObject jsonObject = JSONObject.parseObject(aiResponse);

        String productName = jsonObject.getString("productName");
        int quantity = jsonObject.getIntValue("quantity");

        if (productName == null || quantity <= 0) {
            throw new BusinessException("无法解析 AI 响应，请重新输入");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("productName", productName);
        result.put("quantity", quantity);
        return result;
    }

    /**
     * 生成支付链接（伪代码）
     */
    private String generatePaymentUrl(Integer orderId) {
        return "https://payment.example.com/pay?orderId=" + orderId;
    }
}