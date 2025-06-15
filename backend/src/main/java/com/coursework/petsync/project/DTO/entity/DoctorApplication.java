package com.coursework.petsync.project.DTO.entity;

/**
 * @author HDH
 * @version 1.0
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 医生申请实体类
 */
@Data
@Schema(description = "医生注册申请")
public class DoctorApplication {

    @Schema(description = "申请 ID")
    private Long id;

    @Schema(description = "申请人用户 ID")
    private Long userId;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "执业证书编号")
    private String licenseNumber;

    @Schema(description = "身份证号")
    private String idCardNumber;

    @Schema(description = "医院或机构名称")
    private String hospitalName;

    @Schema(description = "证书图片链接")
    private String certificateUrl;

    @Schema(description = "审核状态（PENDING/APPROVED/REJECTED）")
    private String status;

    @Schema(description = "审核备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}