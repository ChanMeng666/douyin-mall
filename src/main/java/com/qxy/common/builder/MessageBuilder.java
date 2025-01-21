package com.qxy.common.builder;


import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;

public class MessageBuilder {
    public static Message createMessage(Role role, String content) {
        return Message.builder()
                .role(role.getValue())
                .content(content)
                .build();
    }
}
