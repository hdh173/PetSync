package com.coursework.petsync.project.Controller;

import com.coursework.petsync.project.DTO.Request.ApplyIdentityRequest;
import com.coursework.petsync.project.DTO.Request.DoctorProfileRequest;
import com.coursework.petsync.project.DTO.Request.DoctorProfileUpdateRequest;
import com.coursework.petsync.project.DTO.Response.DoctorProfileResponse;
import com.coursework.petsync.project.DTO.Response.PetsyncResponse;
import com.coursework.petsync.project.Service.DoctorService;
import com.coursework.petsync.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author HDH
 * @version 1.0
 */
@RestController
@RequestMapping("/doctor")
@Tag(name = "医生身份认证模块", description = "提供医生申请身份认证接口")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/{id}/identity-apply")
    @Operation(
            summary = "医生身份认证申请",
            description = "医生提交身份认证信息，系统将进入待审核状态",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "申请提交成功",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PetsyncResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "申请失败，可能为重复提交或信息不完整",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PetsyncResponse.class)
                            )
                    )
            }
    )
    public PetsyncResponse<?> applyIdentity(
            @PathVariable("id") Long userId,
            @RequestBody ApplyIdentityRequest applyRequest
    ) {
        // 检查是否已有待处理申请
        if (doctorService.hasPendingApplication(userId)) {
            return PetsyncResponse.failure(400, "您已有待审核的申请，无法重复提交");
        }

        boolean submitted = doctorService.submitApplication(
                userId,
                applyRequest.getRealName(),
                applyRequest.getLicenseNumber(),
                applyRequest.getIdCardNumber(),
                applyRequest.getHospitalName(),
                applyRequest.getCertificateUrl()
        );

        if (submitted) {
            return PetsyncResponse.success("身份认证申请已提交，请等待管理员审核");
        } else {
            return PetsyncResponse.failure(400, "身份申请提交失败，请检查提交信息");
        }
    }

    @PostMapping("/{userId}/update")
    @Operation(
            summary = "医生更新个人信息",
            description = "医生可修改展示在平台的个人简介、头像、专业方向等信息",
            responses = @ApiResponse(responseCode = "200", description = "更新成功")
    )
    public PetsyncResponse<?> updateProfile(@PathVariable Long userId, @RequestBody DoctorProfileRequest request) {
        boolean updated = doctorService.updateDoctorProfile(userId, request);
        return updated ? PetsyncResponse.success("更新成功") : PetsyncResponse.failure(400, "更新失败");
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "获取医生展示信息",
            description = "供用户查看医生个人主页信息",
            responses = @ApiResponse(responseCode = "200", description = "获取成功")
    )
    public PetsyncResponse<?> getDoctorProfile(@PathVariable Long userId) {
        DoctorProfileResponse response = doctorService.getDoctorProfile(userId);
        return response != null ? PetsyncResponse.success(response) : PetsyncResponse.failure(404, "医生不存在");
    }
}
