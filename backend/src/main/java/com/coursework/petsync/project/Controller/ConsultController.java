package com.coursework.petsync.project.Controller;

import com.coursework.petsync.project.DTO.Request.ChatMessageRequest;
import com.coursework.petsync.project.DTO.Request.ConsultRequest;
import com.coursework.petsync.project.DTO.Request.RatingRequest;
import com.coursework.petsync.project.DTO.Request.SummaryRequest;
import com.coursework.petsync.project.DTO.Response.ConsultHistoryResponse;
import com.coursework.petsync.project.DTO.Response.DoctorOnlineResponse;
import com.coursework.petsync.project.DTO.Response.PetsyncResponse;
import com.coursework.petsync.project.Service.ConsultService;
import com.coursework.petsync.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 在线问诊统一控制器
 * URL 前缀: /consult/...
 */
@RestController
@RequestMapping("/consult")
@Tag(name = "宠物在线问诊模块")
@RequiredArgsConstructor
public class ConsultController {

    private final ConsultService consultService;
    private final JwtUtil jwtUtil;

    /* ========== 1. 在线医生列表 ========= */
    @GetMapping("/online")
    @Operation(summary = "获取在线医生列表")
    public PetsyncResponse<List<DoctorOnlineResponse>> onlineDoctors(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag
    ) {
        return PetsyncResponse.success(consultService.listOnlineDoctors(keyword, tag));
    }

    /* ========== 2. 发起问诊 ========= */
    @PostMapping("/request")
    @Operation(summary = "用户发起问诊请求")
    public PetsyncResponse<?> requestConsult(@RequestBody ConsultRequest req,
                                             @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.getUserId(token.substring(7));
        Long sessionId = consultService.createConsult(userId, req);
        return PetsyncResponse.success(sessionId);
    }

    /* ========== 3. 医生接受 / 拒绝 ========= */
    @PostMapping("/accept/{sessionId}")
    @Operation(summary = "医生接受问诊")
    public PetsyncResponse<?> accept(@PathVariable Long sessionId,
                                     @RequestHeader("Authorization") String token) {
        Long doctorId = jwtUtil.getUserId(token.substring(7));
        return consultService.acceptConsult(doctorId, sessionId)
                ? PetsyncResponse.success("已接受")
                : PetsyncResponse.failure(400, "无法接受");
    }

    @PostMapping("/reject/{sessionId}")
    @Operation(summary = "医生拒绝问诊")
    public PetsyncResponse<?> reject(@PathVariable Long sessionId,
                                     @RequestParam String reason,
                                     @RequestHeader("Authorization") String token) {
        Long doctorId = jwtUtil.getUserId(token.substring(7));
        return consultService.rejectConsult(doctorId, sessionId, reason)
                ? PetsyncResponse.success("已拒绝")
                : PetsyncResponse.failure(400, "拒绝失败");
    }

    /* ========== 4. 发送消息 (HTTP 方式; WebSocket 可分离) ========= */
    @PostMapping("/message")
    @Operation(summary = "发送消息（文本/图片）")
    public PetsyncResponse<?> sendMessage(@RequestBody ChatMessageRequest msg,
                                          @RequestHeader("Authorization") String token) {
        Long senderId = jwtUtil.getUserId(token.substring(7));
        String role = jwtUtil.getUserRole(token.substring(7)); // user / doctor
        return consultService.sendMessage(senderId, role, msg)
                ? PetsyncResponse.success("发送成功")
                : PetsyncResponse.failure(400, "发送失败");
    }

    /* ========== 5. 医生提交总结 ========= */
    @PostMapping("/summary")
    @Operation(summary = "医生提交问诊总结")
    public PetsyncResponse<?> submitSummary(@RequestBody SummaryRequest summary,
                                            @RequestHeader("Authorization") String token) {
        Long doctorId = jwtUtil.getUserId(token.substring(7));
        return consultService.submitSummary(doctorId, summary)
                ? PetsyncResponse.success("总结已提交")
                : PetsyncResponse.failure(400, "提交失败");
    }

    /* ========== 6. 用户评分 ========= */
    @PostMapping("/rate")
    @Operation(summary = "用户对问诊进行评分")
    public PetsyncResponse<?> rate(@RequestBody RatingRequest rating,
                                   @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.getUserId(token.substring(7));
        return consultService.rateConsult(userId, rating)
                ? PetsyncResponse.success("评分成功")
                : PetsyncResponse.failure(400, "评分失败");
    }

    /* ========== 7. 会话历史 ========= */
    @GetMapping("/history")
    @Operation(summary = "查看我的问诊历史（用户/医生均可）")
    public PetsyncResponse<List<ConsultHistoryResponse>> history(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.getUserId(token.substring(7));
        return PetsyncResponse.success(consultService.history(userId));
    }

    /* ========== 8. 医生值班 / 离岗 ========= */
    @PostMapping("/status")
    @Operation(summary = "医生设置在线状态（值班/离岗）")
    public PetsyncResponse<?> setStatus(@RequestParam boolean online,
                                        @RequestHeader("Authorization") String token) {
        Long doctorId = jwtUtil.getUserId(token.substring(7));
        consultService.setDoctorOnlineStatus(doctorId, online);
        return PetsyncResponse.success("状态已更新");
    }
}