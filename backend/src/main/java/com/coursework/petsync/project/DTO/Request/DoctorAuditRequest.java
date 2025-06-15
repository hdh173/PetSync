package com.coursework.petsync.project.DTO.Request;

import lombok.Data;

@Data
public class DoctorAuditRequest {
    private String status; // "APPROVED" or "REJECTED"
    private String remark; // 管理员备注
}
