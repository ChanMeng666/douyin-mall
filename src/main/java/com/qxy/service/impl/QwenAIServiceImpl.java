package com.qxy.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.qxy.service.QwenAIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: water
 * Description: 通义千问 AI 服务实现类，实现 QwenAIService 接口
 * Date: 2025/1/24 21:26
 * Version: 1.0
 */
@Service
public class QwenAIServiceImpl implements QwenAIService {

    @Value("${dashscope.api-key}")
    private String apiKey;

    @Override
    public String callQwenAI(String userInput) throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        List<Message> messages = new ArrayList<>();
        messages.add(Message.builder().role(Role.SYSTEM.getValue()).content("You are a helpful assistant.").build());
        messages.add(Message.builder().role(Role.USER.getValue()).content(userInput).build());

        GenerationParam param = GenerationParam.builder()
                .apiKey(apiKey)
                .model("qwen-plus")
                .messages(messages)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();

        GenerationResult result = gen.call(param);
        return result.getOutput().getChoices().get(0).getMessage().getContent();
    }
}