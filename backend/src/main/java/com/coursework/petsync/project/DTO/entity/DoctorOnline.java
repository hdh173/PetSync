package com.coursework.petsync.project.DTO.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author HDH
 * @version 1.0
 */
@Data
@Schema(description = "医生基本信息")
public class DoctorOnline {
    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "医生姓名")
    private String name;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "医生专业标签")
    private List<String> specializationTags;
}