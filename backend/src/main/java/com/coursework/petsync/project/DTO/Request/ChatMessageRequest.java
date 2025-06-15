package com.coursework.petsync.project.DTO.Request;

import lombok.Data;

// Request WebSocket / HTTP 发送消息
@Data
public class ChatMessageRequest {
    private Long sessionId;
    private String messageType;  // text / image
    private String content;
}
