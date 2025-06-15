package com.coursework.petsync.project.DTO.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "会话消息")
public class ConsultMessage {
    private Long id;
    private Long sessionId;
    private Long senderId;
    private String senderRole;   // user / doctor
    private String messageType;  // text / image
    private String content;
    private LocalDateTime sendTime;
    private Boolean isRead;
}