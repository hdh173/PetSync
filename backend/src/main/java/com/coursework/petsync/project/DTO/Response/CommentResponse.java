package com.coursework.petsync.project.DTO.Response;

import lombok.Data;

import java.util.Date;

@Data
public class CommentResponse {
    private Long id;
    private Long postId;
    private Long userId;
    private String content;
    private Date createTime;
    private String userName;
    private String userAvatar;
}