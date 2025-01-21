package com.qxy.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.qxy.model.req.ChatRequest;
import com.qxy.model.res.ChatResponse;
import com.qxy.service.AiService;
import com.qxy.common.builder.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AiServiceImpl implements AiService {

    @Override
    public ChatResponse chat(ChatRequest chatRequest) {
        try {
            List<Message> messages = new ArrayList<>();
            messages.add(MessageBuilder.createMessage(Role.SYSTEM, "You are a helpful assistant."));
            messages.add(MessageBuilder.createMessage(Role.USER, chatRequest.getUserInput()));

            GenerationParam param = GenerationParam.builder()
                    .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                    .model("qwen-plus")
                    .messages(messages)
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                    .build();

            Generation gen = new Generation();
            GenerationResult result = gen.call(param);

            String aiResponse = result.getOutput().getChoices().get(0).getMessage().getContent();
            return new ChatResponse(aiResponse);
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            e.printStackTrace();
            return new ChatResponse("Error: " + e.getMessage());
        }
    }
}
