package com.qxy.test.dao;

import com.qxy.dao.AiOrderDao;
import com.qxy.model.po.AiOrder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "spring.task.scheduling.enabled=false",
        "spring.scheduling.enabled=false",
        "spring.task.execution.enabled=false",
        "spring.main.allow-bean-definition-overriding=true"
})
@ActiveProfiles("test")
public class AiOrderDaoTest {

    @Autowired
    private AiOrderDao aiOrderDao;

    private static final Integer TEST_USER_ID = 10000;

    private Integer testOrderId;

    @Before
    public void setup() {
        AiOrder order = new AiOrder();
        order.setUserId(TEST_USER_ID);
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setStatus("PAID");
        order.setPayType(1);
        order.setCreatedAt(new Timestamp(new Date().getTime()));
        order.setUpdatedAt(new Timestamp(new Date().getTime()));
        aiOrderDao.insertOrder(order);
        testOrderId = order.getOrderId();
        assertThat(testOrderId).isNotNull();
    }

    @Test
    @Transactional
    public void testFindByUserId() {
        List<AiOrder> orders = aiOrderDao.findByUserId(TEST_USER_ID);
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getTotalAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    public void testFindByOrderId() {
        AiOrder order = aiOrderDao.findByOrderId(testOrderId);
        assertThat(order).isNotNull();
        assertThat(order.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(order.getTotalAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    @Transactional
    public void testInsertOrder() {
        AiOrder newOrder = new AiOrder();
        newOrder.setUserId(TEST_USER_ID + 1);
        newOrder.setTotalAmount(new BigDecimal("200.00"));
        newOrder.setStatus("PENDING");
        newOrder.setPayType(2);
        newOrder.setCreatedAt(new Timestamp(new Date().getTime()));
        newOrder.setUpdatedAt(new Timestamp(new Date().getTime()));
        aiOrderDao.insertOrder(newOrder);

        assertThat(newOrder.getOrderId()).isNotNull();

        AiOrder insertedOrder = aiOrderDao.findByOrderId(newOrder.getOrderId());
        assertThat(insertedOrder).isNotNull();
        assertThat(insertedOrder.getTotalAmount()).isEqualByComparingTo(new BigDecimal("200.00"));
    }
}
