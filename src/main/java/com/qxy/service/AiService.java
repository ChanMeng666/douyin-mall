package com.qxy.service;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.qxy.model.req.ChatRequest;
import com.qxy.model.res.ChatResponse;

public interface AiService {
    /**
     * 处理用户输入并返回 AI 模型的响应
     *
     * @param chatRequest 用户输入
     * @return AI 模型的响应
     */
    ChatResponse chat(ChatRequest chatRequest);
}
