package com.qxy.controller;

import com.qxy.controller.dto.ai.AiOrderRequestDTO;
import com.qxy.controller.dto.ai.AiOrderResponseDTO;
import com.qxy.service.AISimulationOrderService;
import com.qxy.service.impl.AIServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @Autowired
    private AISimulationOrderService aiSimulationOrderService;

    @PostMapping("/query")
    public String askAI(@RequestParam String userInput) {
        return aiServiceImpl.handleUserInput(userInput);
    }

    @PostMapping("/auto")
    public ResponseEntity<AiOrderResponseDTO> createOrder(
            @RequestBody @Valid AiOrderRequestDTO requestDTO) {
        AiOrderResponseDTO response = aiSimulationOrderService.createOrderFromMessage(requestDTO);
        return ResponseEntity.ok(response);
    }
}