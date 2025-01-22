package com.qxy.model.res;

import lombok.Data;

@Data
public class ChatResponse {
    private String aiResponse; // AI 模型的回复

    // 构造方法
    public ChatResponse(String aiResponse) {
        this.aiResponse = aiResponse;
    }

    // Getter 和 Setter
    public String getAiResponse() {
        return aiResponse;
    }

    public void setAiResponse(String aiResponse) {
        this.aiResponse = aiResponse;
    }
}
