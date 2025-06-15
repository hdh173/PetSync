package com.coursework.petsync.project.DTO.Response;

import lombok.Data;

// Response 会话历史
@Data
public class ConsultHistoryResponse {
    private Long sessionId;
    private String doctorName;
    private String doctorAvatar;
    private String petName;
    private java.time.LocalDateTime startTime;
    private java.time.LocalDateTime endTime;
    private String summary;
    private String suggestion;
    private Integer score;
}
