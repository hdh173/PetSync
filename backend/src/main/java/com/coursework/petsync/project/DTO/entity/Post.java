package com.coursework.petsync.project.DTO.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String images;
    private String tags;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer coinCount;
    private Date createTime;
    private Date updateTime;
}