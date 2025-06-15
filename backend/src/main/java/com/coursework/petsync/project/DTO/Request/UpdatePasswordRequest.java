package com.coursework.petsync.project.DTO.Request;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String oldPassword;
    private String newPassword;
}