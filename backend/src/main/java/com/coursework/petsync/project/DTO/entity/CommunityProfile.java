package com.coursework.petsync.project.DTO.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CommunityProfile {
    private Long userId;
    private Integer following;
    private Integer followed;
    private Integer postCount;
    private Integer likeCount;
    private Integer coinCount;
    private Date createTime;
    private Date updateTime;
    
    // 扩展字段
    private String userName;
    private String userAvatar;
    private Boolean isConsultant;
    private Boolean follow;
    private Integer haveCoins;
}