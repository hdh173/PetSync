package com.coursework.petsync.project.Service;

import com.coursework.petsync.project.DTO.Request.ChatMessageRequest;
import com.coursework.petsync.project.DTO.Request.ConsultRequest;
import com.coursework.petsync.project.DTO.Request.RatingRequest;
import com.coursework.petsync.project.DTO.Request.SummaryRequest;
import com.coursework.petsync.project.DTO.Response.ConsultHistoryResponse;
import com.coursework.petsync.project.DTO.Response.DoctorOnlineResponse;
import com.coursework.petsync.project.DTO.entity.ConsultMessage;
import com.coursework.petsync.project.DTO.entity.ConsultSession;
import com.coursework.petsync.project.DTO.entity.DiagnosisRecord;
import com.coursework.petsync.project.Mapper.ConsultMessageMapper;
import com.coursework.petsync.project.Mapper.ConsultSessionMapper;
import com.coursework.petsync.project.Mapper.DiagnosisRecordMapper;
import com.coursework.petsync.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultService {
    @Autowired
    private ConsultSessionMapper sessionMapper;
    @Autowired
    private ConsultMessageMapper messageMapper;
    @Autowired
    private DiagnosisRecordMapper recordMapper;
    @Autowired
    private RedisUtil redisUtil;
    private static final String ONLINE_DOCTOR_KEY_PREFIX = "doctor:online:";  // doctor:online:{doctorId}
    private static final String ONLINE_DOCTOR_SET = "online_doctor_set";

    public List<DoctorOnlineResponse> listOnlineDoctors(String keyword, String tag) {
        // 1. 从 Redis 获取在线 doctorId 列表
        @SuppressWarnings("unchecked")
        java.util.Set<Object> idsObj = (java.util.Set<Object>) redisUtil.get(ONLINE_DOCTOR_SET);
        if (idsObj == null) return java.util.Collections.emptyList();
        java.util.Set<Long> ids = idsObj.stream().map(o -> Long.valueOf(o.toString())).collect(java.util.stream.Collectors.toSet());

        if (ids.isEmpty()) return java.util.Collections.emptyList();

        // 2. 查询 doctor_profile（略）+ 过滤 keyword / tag
        return java.util.Collections.emptyList();
    }

    public Long createConsult(Long userId, ConsultRequest req) {
        ConsultSession s = new ConsultSession();
        s.setUserId(userId);
        s.setDoctorId(req.getDoctorId());
        s.setPetId(req.getPetId());
        s.setStatus("PENDING");
        s.setStartTime(java.time.LocalDateTime.now());
        sessionMapper.insert(s);
        // TODO: 通过 WebSocket / 推送通知医生
        return s.getId();
    }

    public boolean acceptConsult(Long doctorId, Long sessionId) {
        ConsultSession s = sessionMapper.findById(sessionId);
        if (s == null || !s.getDoctorId().equals(doctorId) || !"PENDING".equals(s.getStatus())) return false;
        s.setStatus("ONGOING");
        s.setStartTime(java.time.LocalDateTime.now());
        return sessionMapper.updateStatus(s) > 0;
    }

    public boolean rejectConsult(Long doctorId, Long sessionId, String reason) {
        ConsultSession s = sessionMapper.findById(sessionId);
        if (s == null || !s.getDoctorId().equals(doctorId) || !"PENDING".equals(s.getStatus())) return false;
        s.setStatus("REJECTED");
        sessionMapper.updateStatus(s);
        // TODO: 保存拒绝理由，可放在咨询消息或单独表
        return true;
    }

    public boolean sendMessage(Long senderId, String role, ChatMessageRequest req) {
        ConsultMessage msg = new ConsultMessage();
        msg.setSessionId(req.getSessionId());
        msg.setSenderId(senderId);
        msg.setSenderRole(role);
        msg.setMessageType(req.getMessageType());
        msg.setContent(req.getContent());
        messageMapper.insert(msg);
        // TODO: WebSocket 广播
        return true;
    }

    public boolean submitSummary(Long doctorId, SummaryRequest summary) {
        ConsultSession s = sessionMapper.findById(summary.getSessionId());
        if (s == null || !s.getDoctorId().equals(doctorId) || !"ONGOING".equals(s.getStatus())) return false;
        DiagnosisRecord record = new DiagnosisRecord();
        record.setSessionId(s.getId());
        record.setDoctorId(doctorId);
        record.setPetId(s.getPetId());
        record.setContent(summary.getContent());
        record.setSuggestion(summary.getSuggestion());
        recordMapper.insert(record);
        s.setStatus("FINISHED_WAIT_RATE");
        s.setEndTime(java.time.LocalDateTime.now());
        sessionMapper.updateStatus(s);
        // 设置 12h 自动好评
        redisUtil.set("consult:autoRate:" + s.getId(), "1", 720);
        return true;
    }

    public boolean rateConsult(Long userId, RatingRequest rating) {
        ConsultSession s = sessionMapper.findById(rating.getSessionId());
        if (s == null || !s.getUserId().equals(userId) || !"FINISHED_WAIT_RATE".equals(s.getStatus())) return false;
        // TODO: 保存评分 doctor_review
        s.setStatus("FINISHED");
        sessionMapper.updateStatus(s);
        // 删除自动评分键
        redisUtil.delete("consult:autoRate:" + s.getId());
        return true;
    }

    public List<ConsultHistoryResponse> history(Long userId) {
        // TODO: 查询会话、诊断、评分并组装
        return java.util.Collections.emptyList();
    }

    public void setDoctorOnlineStatus(Long doctorId, boolean online) {
        if (online) {
            redisUtil.set(ONLINE_DOCTOR_KEY_PREFIX + doctorId, true, 0);
            // 把 doctorId 加入在线集合
            java.util.Set<Object> set = (java.util.Set<Object>) redisUtil.get(ONLINE_DOCTOR_SET);
            if (set == null) set = new java.util.HashSet<>();
            set.add(doctorId);
            redisUtil.set(ONLINE_DOCTOR_SET, set, 0);
        } else {
            redisUtil.delete(ONLINE_DOCTOR_KEY_PREFIX + doctorId);
            java.util.Set<Object> set = (java.util.Set<Object>) redisUtil.get(ONLINE_DOCTOR_SET);
            if (set != null) {
                set.remove(doctorId);
                redisUtil.set(ONLINE_DOCTOR_SET, set, 0);
            }
        }
    }
}

