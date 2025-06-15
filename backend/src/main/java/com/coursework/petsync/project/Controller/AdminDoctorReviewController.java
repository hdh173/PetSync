package com.coursework.petsync.project.Controller;

import com.coursework.petsync.project.DTO.Request.DoctorAuditRequest;
import com.coursework.petsync.project.DTO.Response.DoctorApplicationResponse;
import com.coursework.petsync.project.DTO.Response.PetsyncResponse;
import com.coursework.petsync.project.Service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "管理员审核模块", description = "医生资质审核接口")
public class AdminDoctorReviewController {

    @Autowired
    private DoctorService doctorApplicationService;

    @PostMapping("/{applicationId}/review")
    @Operation(
            summary = "审核医生申请",
            description = "管理员审核医生的认证申请，操作为通过或拒绝",
            responses = @ApiResponse(responseCode = "200", description = "审核操作成功")
    )
    public PetsyncResponse<?> reviewApplication(
            @PathVariable Long applicationId,
            @RequestParam("approve") boolean approve,
            @RequestParam(value = "remark", required = false) String remark
    ) {
        boolean result = doctorApplicationService.reviewApplication(applicationId, approve, remark);
        return result ? PetsyncResponse.success("审核完成") : PetsyncResponse.failure(400, "审核失败或申请不存在");
    }

    @GetMapping("/pending")
    @Operation(
            summary = "获取待审核申请列表",
            description = "获取所有待审核状态的医生资质申请记录",
            responses = @ApiResponse(responseCode = "200", description = "成功返回待审核申请列表")
    )
    public PetsyncResponse<List<DoctorApplicationResponse>> getPendingApplications() {
        List<DoctorApplicationResponse> list = doctorApplicationService.getPendingApplications();
        return PetsyncResponse.success(list);
    }
    @GetMapping("/reports")
    public PetsyncResponse<?> listUnprocessedReports() {
        return null; // TODO
    }

    @GetMapping("/reports/{reportId}/post")
    public PetsyncResponse<?> getReportedPost(@PathVariable Long reportId) {
        return null; // TODO
    }

    @PostMapping("/reports/{reportId}/process")
    public PetsyncResponse<?> processReport(
            @PathVariable Long reportId,
            @RequestParam  boolean deletePost,
            @RequestBody String feedback) {
        return null; // TODO
    }

}
