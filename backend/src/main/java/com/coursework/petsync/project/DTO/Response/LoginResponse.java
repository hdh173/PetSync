package com.coursework.petsync.project.DTO.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author HDH
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@Schema(description = "用户登录响应结果")
public class LoginResponse {
    private Long id;
    private String name;
    private String role;
    private String access_token;
    private final String token_type = "Bearer";
    private Long expires_in;
    private String refresh_token;
    private String avatar;

}
