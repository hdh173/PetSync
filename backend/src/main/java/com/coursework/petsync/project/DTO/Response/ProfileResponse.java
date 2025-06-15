package com.coursework.petsync.project.DTO.Response;

import lombok.*;

/**
 * @author HDH
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class ProfileResponse {
    private String username;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private String gender;
    private String signature;
}
