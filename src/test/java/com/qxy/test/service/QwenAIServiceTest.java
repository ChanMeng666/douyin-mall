package com.qxy.test.service;

import com.qxy.service.QwenAIService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
public class QwenAIServiceTest {
/**
 * @author 朱先生
 * @description: TODO
 * @date 2025/2/10 12:35
 * @version 1.0
 */

    @Autowired
    private QwenAIService qwenAIService;

    @Test
    public void testCallQwenAI() {
        try {
            String userInput = "我想买2个iPhone 14";
            String response = qwenAIService.callQwenAI(userInput);
            System.out.println("AI 响应: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
