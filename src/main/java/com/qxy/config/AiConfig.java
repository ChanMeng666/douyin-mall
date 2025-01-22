package com.qxy.config;

import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.common.Message;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfig {

    @Bean
    public GenerationParam generationParam(List<Message> messages) {
        return GenerationParam.builder()
                .apiKey("sk-0c9891cfaab947c2aef970b6a89220fd")
                .model("qwen-plus")
                .messages(messages)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
    }
}
