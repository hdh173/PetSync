package com.coursework.petsync.project.DTO.Request;

import lombok.Data;

// Request 用户评分
@Data
public class RatingRequest {
    private Long sessionId;
    private Integer score;  // 1-5
}
