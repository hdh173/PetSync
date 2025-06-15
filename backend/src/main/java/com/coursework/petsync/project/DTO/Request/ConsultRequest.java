package com.coursework.petsync.project.DTO.Request;

import lombok.Data;

// Request 发起问诊
@Data
public class ConsultRequest {
    private Long doctorId;
    private Long petId;
    private String initialDescription;   // 问题描述
}