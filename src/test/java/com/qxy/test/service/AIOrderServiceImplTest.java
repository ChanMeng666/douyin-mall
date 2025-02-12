package com.qxy.test.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import com.qxy.dao.AiOrderDao;
import com.qxy.model.po.AiOrder;
import com.qxy.service.impl.AIOrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AIOrderServiceImplTest {

    @Mock
    private AiOrderDao aiOrderDao;

    @InjectMocks
    private AIOrderServiceImpl aiOrderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrdersByUserId() {
        // Mock data
        Integer userId = 1;
        AiOrder order1 = new AiOrder();
        order1.setOrderId(101);
        order1.setUserId(userId);
        order1.setTotalAmount(new BigDecimal("100.00"));
        order1.setStatus("PAID");
        order1.setPayType(1);
        order1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order1.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        AiOrder order2 = new AiOrder();
        order2.setOrderId(102);
        order2.setUserId(userId);
        order2.setTotalAmount(new BigDecimal("200.00"));
        order2.setStatus("PENDING");
        order2.setPayType(2);
        order2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order2.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        List<AiOrder> mockOrders = Arrays.asList(order1, order2);

        // Mock behavior
        when(aiOrderDao.findByUserId(userId)).thenReturn(mockOrders);

        // Call the method
        List<AiOrder> result = aiOrderService.getOrdersByUserId(userId);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(101, result.get(0).getOrderId());
        assertEquals(102, result.get(1).getOrderId());

        // Verify interactions
        verify(aiOrderDao, times(1)).findByUserId(userId);
    }

    @Test
    public void testGetOrderByOrderId() {
        // Mock data
        Integer orderId = 101;
        AiOrder mockOrder = new AiOrder();
        mockOrder.setOrderId(orderId);
        mockOrder.setUserId(1);
        mockOrder.setTotalAmount(new BigDecimal("100.00"));
        mockOrder.setStatus("PAID");
        mockOrder.setPayType(1);
        mockOrder.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        mockOrder.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        // Mock behavior
        when(aiOrderDao.findByOrderId(orderId)).thenReturn(mockOrder);

        // Call the method
        AiOrder result = aiOrderService.getOrderByOrderId(orderId);

        // Assertions
        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        assertEquals("PAID", result.getStatus());

        // Verify interactions
        verify(aiOrderDao, times(1)).findByOrderId(orderId);
    }
}