package com.qxy.test.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;

import com.qxy.common.exception.BusinessException;
import com.qxy.controller.dto.ai.AiOrderRequestDTO;
import com.qxy.controller.dto.ai.AiOrderResponseDTO;
import com.qxy.dao.AiOrderDao;
import com.qxy.dao.AiProductDao;
import com.qxy.model.enums.ProductStatus;
import com.qxy.model.po.AiOrder;
import com.qxy.model.po.OrderItems;
import com.qxy.model.po.Product;
import com.qxy.service.QwenAIService;
import com.qxy.service.impl.AISimulationOrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class AISimulationOrderServiceImplTest {

    @Mock
    private AiProductDao aiProductMapper;

    @Mock
    private AiOrderDao aiOrderMapper;

    @Mock
    private QwenAIService qwenAIService;

    @InjectMocks
    private AISimulationOrderServiceImpl aiSimulationOrderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrderFromMessage_Success() throws Exception {
        // Mock input
        AiOrderRequestDTO requestDTO = new AiOrderRequestDTO();
        requestDTO.setUserId(1);
        requestDTO.setMessage("我想买2个手机");

        // Mock AI response
        String aiResponse = "{\"productName\": \"手机\", \"quantity\": 2}";
        when(qwenAIService.callQwenAI(requestDTO.getMessage())).thenReturn(aiResponse);

        // Mock product data
        Product product = new Product();
        product.setProductId(101);
        product.setName("手机");
        product.setDescription("智能手机");
        product.setPrice(new BigDecimal("500.00"));
        product.setStock(10);
        product.setImageUrl("http://example.com/image.jpg");
        product.setStatus(ProductStatus.ACTIVE);
        product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        when(aiProductMapper.searchProductsByName("手机")).thenReturn(Collections.singletonList(product));

        // Mock order insertion
        doNothing().when(aiOrderMapper).insertOrder(any(AiOrder.class));

        // Mock order item insertion
        doNothing().when(aiOrderMapper).insertOrderItem(any(OrderItems.class));

        // Mock stock decrease
        doNothing().when(aiProductMapper).decreaseStock(101, 2);

        // Call the method
        AiOrderResponseDTO response = aiSimulationOrderService.createOrderFromMessage(requestDTO);

        // Assertions
        assertNotNull(response);
        assertTrue(response.getOrderId().matches("\\d+")); // Ensure orderId is a number
        assertEquals("等待支付", response.getStatus());

        // Verify interactions
        verify(qwenAIService, times(1)).callQwenAI(requestDTO.getMessage());
        verify(aiProductMapper, times(1)).searchProductsByName("手机");
        verify(aiOrderMapper, times(1)).insertOrder(any(AiOrder.class));
        verify(aiOrderMapper, times(1)).insertOrderItem(any(OrderItems.class));
        verify(aiProductMapper, times(1)).decreaseStock(101, 2);
    }

    @Test
    public void testCreateOrderFromMessage_ProductNotFound() throws Exception {
        // Mock input
        AiOrderRequestDTO requestDTO = new AiOrderRequestDTO();
        requestDTO.setUserId(1);
        requestDTO.setMessage("我想买2个手机");

        // Mock AI response
        String aiResponse = "{\"productName\": \"手机\", \"quantity\": 2}";
        when(qwenAIService.callQwenAI(requestDTO.getMessage())).thenReturn(aiResponse);

        // Mock product data (no products found)
        when(aiProductMapper.searchProductsByName("手机")).thenReturn(Collections.emptyList());

        // Call the method and expect exception
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            aiSimulationOrderService.createOrderFromMessage(requestDTO);
        });

        // Assertions
        assertEquals("未找到商品：手机", exception.getMessage());

        // Verify interactions
        verify(qwenAIService, times(1)).callQwenAI(requestDTO.getMessage());
        verify(aiProductMapper, times(1)).searchProductsByName("手机");
    }
}
