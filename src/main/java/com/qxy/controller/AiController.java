package com.qxy.controller;

import com.qxy.model.req.ChatRequest;
import com.qxy.model.res.ChatResponse;
import com.qxy.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest chatRequest) {
        return aiService.chat(chatRequest);
    }
}
