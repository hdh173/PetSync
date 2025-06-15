package com.coursework.petsync.project.DTO.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户对医生评价记录实体类
 */
@Data
@Schema(description = "医生评价实体")
public class DoctorReview {

    @Schema(description = "主键ID", example = "7001")
    private Long id;

    @Schema(description = "关联的问诊会话ID", example = "1001")
    private Long sessionId;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "医生ID", example = "20")
    private Long doctorId;

    @Schema(description = "评分（1-5）", example = "5")
    private Integer rating;

    @Schema(description = "评价内容", example = "医生很耐心，建议很专业")
    private String comment;

    @Schema(description = "评价时间")
    private LocalDateTime createdAt;
}