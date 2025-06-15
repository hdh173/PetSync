package com.coursework.petsync.project.DTO.entity;

/**
 * @author HDH
 * @version 1.0
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 用户举报内容实体
 */
@Data
@Schema(description = "举报内容记录")
public class ContentReport {

    @Schema(description = "举报 ID")
    private Long id;

    @Schema(description = "举报人 ID")
    private Long reporterId;

    @Schema(description = "被举报人 ID")
    private Long reportedUserId;

    @Schema(description = "内容类型（comment/post/doctor）")
    private String contentType;

    @Schema(description = "被举报内容 ID")
    private Long contentId;

    @Schema(description = "举报理由")
    private String reason;

    @Schema(description = "状态：PENDING/RESOLVED/REJECTED")
    private String status;

    @Schema(description = "处理人 ID")
    private Long handlerId;

    @Schema(description = "处理结果备注")
    private String handleResult;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}