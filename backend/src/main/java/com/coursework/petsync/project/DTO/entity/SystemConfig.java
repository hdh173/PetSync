package com.coursework.petsync.project.DTO.entity;

/**
 * @author HDH
 * @version 1.0
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 系统配置项
 */
@Data
@Schema(description = "系统配置")
public class SystemConfig {

    @Schema(description = "配置 ID")
    private Long id;

    @Schema(description = "配置键")
    private String configKey;

    @Schema(description = "配置值")
    private String configValue;

    @Schema(description = "配置描述")
    private String description;

    @Schema(description = "更新时间")
    private Date updateTime;
}