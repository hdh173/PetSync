package com.coursework.petsync.project.DTO.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "医生申请响应信息")
public class DoctorApplicationResponse {
    
    @Schema(description = "申请ID")
    private Long id;

    @Schema(description = "医生用户ID")
    private Long userId;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "证件编号")
    private String licenseNumber;

    @Schema(description = "提交时间")
    private LocalDateTime submittedAt;

    @Schema(description = "当前状态")
    private String status;
}
