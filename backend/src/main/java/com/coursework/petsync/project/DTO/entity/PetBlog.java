package com.coursework.petsync.project.DTO.entity;

/**
 * @author HDH
 * @version 1.0
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 宠物动态博客实体类
 */
@Data
@Schema(description = "宠物动态博客")
public class PetBlog {

    @Schema(description = "博客ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "标签，英文逗号分隔")
    private String tags;

    @Schema(description = "是否草稿")
    private Boolean isDraft;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "评论数")
    private Integer commentCount;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}