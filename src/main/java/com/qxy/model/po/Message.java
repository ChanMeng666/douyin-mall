package com.qxy.model.po;

import com.alibaba.dashscope.common.Role;

public class Message {
    private String role; // 角色，如 "system" 或 "user"
    private String content; // 消息内容

    // 构造方法
    public Message(Role role, String content) {
        this.role = role.getValue();
        this.content = content;
    }

    // Getter 和 Setter
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
