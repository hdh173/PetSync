package com.coursework.petsync.project.DTO.Request;

import lombok.Data;

// Request 医生提交结论
@Data
public class SummaryRequest {
    private Long sessionId;
    private String content;
    private String suggestion;
}
