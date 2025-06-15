package com.coursework.petsync.project.DTO.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "在线问诊会话")
public class ConsultSession {
    private Long id;
    private Long userId;
    private Long doctorId;
    private Long petId;
    private String status;      // PENDING / REJECTED / ONGOING / FINISHED_WAIT_RATE / FINISHED
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}