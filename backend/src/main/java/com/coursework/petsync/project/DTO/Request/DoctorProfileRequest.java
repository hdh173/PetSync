package com.coursework.petsync.project.DTO.Request;

import lombok.Data;

@Data
public class DoctorProfileRequest {
    private String specialty;
    private String title;
    private String experience;
    private String achievements;
    private String profilePictureUrl;
}
