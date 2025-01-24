package com.qxy.controller;

import com.qxy.service.impl.AIServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Author: water
 * Description: AI 控制器，提供 AI 问答功能的 REST API
 * Date: 2025/1/24 21:26
 * Version: 1.0
 */
@RestController
@RequestMapping("/api/ai/order")
public class ChatController {

    @Autowired
    private AIServiceImpl aiServiceImpl;

    @PostMapping("/query")
    public String askAI(@RequestParam String userInput) {
        return aiServiceImpl.handleUserInput(userInput);
    }
}