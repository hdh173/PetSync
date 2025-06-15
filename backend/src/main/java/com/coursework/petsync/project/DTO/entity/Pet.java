package com.coursework.petsync.project.DTO.entity;

/**
 * @author HDH
 * @version 1.0
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 宠物实体类
 * 一个用户可添加多只宠物档案
 */
@Data
@Schema(name = "Pet", description = "宠物实体")
public class Pet implements Serializable {

    @Schema(description = "宠物ID")
    private Long id;

    @Schema(description = "所属用户ID")
    private Long userId;

    @Schema(description = "宠物名称")
    private String name;

    @Schema(description = "宠物类型（如狗、猫）")
    private String type;

    @Schema(description = "品种")
    private String breed;

    @Schema(description = "性别（0-未知，1-公，2-母）")
    private Integer gender;

    @Schema(description = "出生日期")
    private LocalDate birthday;

    @Schema(description = "宠物头像")
    private String avatarUrl;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;
}