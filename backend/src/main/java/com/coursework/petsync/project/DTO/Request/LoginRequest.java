package com.coursework.petsync.project.DTO.Request;

import lombok.Data;

/**
 * @author HDH
 * @version 1.0
 */
@Data
public class LoginRequest {
    private String usernameOrEmail;
    private String password;
}
