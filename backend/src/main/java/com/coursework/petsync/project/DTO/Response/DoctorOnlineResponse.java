package com.coursework.petsync.project.DTO.Response;

import lombok.Data;

// Response 在线医生
@Data
public class DoctorOnlineResponse {
    private Long doctorId;
    private String name;
    private String avatarUrl;
    private java.util.List<String> specializationTags;
}
