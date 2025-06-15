package com.coursework.petsync.project.DTO.entity;

/**
 * @author HDH
 * @version 1.0
 */

import com.coursework.petsync.project.DTO.Request.UserUpdateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 用于表示用户的基本信息，支持普通用户、医生、管理员角色
 */
@Data
@Schema(name = "User", description = "用户实体")
public class User implements Serializable {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像URL")
    private String avatarUrl;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "用户手机号")
    private String phone;

    @Schema(description = "用户角色USER/DOCTOR?ADMIN")
    private String role;

    @Schema(description = "用户性别MALE/FEMALE")
    private String gender;

    @Schema(description = "用户签名")
    private String signature;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;


    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User() {

    }
}