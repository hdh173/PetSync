package com.coursework.petsync.project.DTO.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DoctorProfile {
    private Long id;
    private Long userId;
    private String specialty;
    private String title;
    private String experience;
    private String achievements;
    private String profilePictureUrl;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}