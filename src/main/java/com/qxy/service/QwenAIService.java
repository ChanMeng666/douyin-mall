package com.qxy.service;

/**
 * Author: water
 * Description: 通义千问 AI 服务接口，定义调用 AI 模型的方法
 * Date: 2025/1/24 21:26
 * Version: 1.0
 */
public interface QwenAIService {
    String callQwenAI(String userInput) throws Exception;
}
