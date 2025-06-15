package com.coursework.petsync.project.DTO.Request;

import lombok.Data;

@Data
public class DoctorProfileUpdateRequest {
    private String nickname;
    private String avatarUrl;
    private String department;
    private String title;
    private Integer yearsOfExperience;
    private String goodAt;
    private String introduction;
}
