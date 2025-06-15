package com.coursework.petsync.project.DTO.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author HDH
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String username;
    private String nickname;
    private MultipartFile avatarUrl;
    private String phone;
    private String gender;
    private String signature;

}
