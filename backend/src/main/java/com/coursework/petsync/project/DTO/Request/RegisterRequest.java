package com.coursework.petsync.project.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author HDH
 * @version 1.0
 */
@Data
@Schema(description = "用户注册请求")
public class RegisterRequest {
    @Schema(description = "用户邮箱", example = "test@example.com")
    private String email;

    @Schema(description = "邮箱验证码", example = "123456")
    private String code;

    @Schema(description = "密码（明文，后端加密）", example = "123456")
    private String password;

    @Schema(description = "用户名", example = "托尼·布莱克")
    private String username;
}
