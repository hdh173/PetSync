package com.coursework.petsync.project.DTO.Response;

import lombok.Data;

@Data
public class DoctorProfileResponse {
    private Long userId;
    private String specialty;
    private String title;
    private String experience;
    private String achievements;
    private String profilePictureUrl;
    private Double rating;
    private Long visitCount;
}