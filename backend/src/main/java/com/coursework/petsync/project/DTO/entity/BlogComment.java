package com.coursework.petsync.project.DTO.entity;

/**
 * @author HDH
 * @version 1.0
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 博文评论实体类
 */
@Data
@Schema(description = "博文评论")
public class BlogComment {

    @Schema(description = "评论ID")
    private Long id;

    @Schema(description = "博客ID")
    private Long blogId;

    @Schema(description = "评论用户ID")
    private Long userId;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "创建时间")
    private Date createTime;
}