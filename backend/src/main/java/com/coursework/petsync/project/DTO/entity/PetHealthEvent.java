package com.coursework.petsync.project.DTO.entity;

/**
 * @author HDH
 * @version 1.0
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 宠物健康事件实体类
 */
@Data
@Schema(description = "宠物健康事件")
public class PetHealthEvent {

    @Schema(description = "事件ID")
    private Long id;

    @Schema(description = "宠物ID")
    private Long petId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "事件类型")
    private String eventType;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "事件时间")
    private Date eventTime;

    @Schema(description = "是否提醒")
    private Boolean remind;

    @Schema(description = "重复周期（none/daily/weekly/monthly）")
    private String repeatType;

    @Schema(description = "创建时间")
    private Date createTime;
}
