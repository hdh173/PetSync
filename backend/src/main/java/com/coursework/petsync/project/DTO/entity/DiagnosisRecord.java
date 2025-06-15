package com.coursework.petsync.project.DTO.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "诊疗结论记录")
public class DiagnosisRecord {
    private Long id;
    private Long sessionId;
    private Long doctorId;
    private Long petId;
    private String content;
    private String suggestion;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
