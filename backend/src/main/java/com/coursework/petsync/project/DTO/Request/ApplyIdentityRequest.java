package com.coursework.petsync.project.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author HDH
 * @version 1.0
 */
@Data
public class ApplyIdentityRequest {

    @Schema(description = "真实姓名", example = "张医生")
    private String realName;

    @Schema(description = "执业证书编号", example = "DOC20240501")
    private String licenseNumber;

    @Schema(description = "身份证号", example = "11010119900307001X")
    private String idCardNumber;

    @Schema(description = "所在医院或机构名称", example = "北京宠物医院")
    private String hospitalName;

    @Schema(description = "资质证书图片链接", example = "https://example.com/certificate.jpg")
    private String certificateUrl;
}