package com.coursework.petsync.project.Mapper;

import com.coursework.petsync.project.DTO.entity.ConsultSession;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConsultMapper {

    /**
     * 创建新的问诊会话
     * @param session 问诊会话实体
     * @return 插入成功返回1
     */
    @Insert("INSERT INTO consult_session (user_id, doctor_id, pet_id, start_time, status, conclusion) " +
            "VALUES (#{userId}, #{doctorId}, #{petId}, #{startTime}, #{status}, #{conclusion})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createConsultSession(ConsultSession session);

    /**
     * 通过会话ID查询问诊会话
     * @param sessionId 会话ID
     * @return 会话实体
     */
    @Select("SELECT * FROM consult_session WHERE id = #{sessionId}")
    ConsultSession getConsultSessionById(Long sessionId);

    /**
     * 获取用户的所有问诊记录
     * @param userId 用户ID
     * @return 会话列表
     */
    @Select("SELECT * FROM consult_session WHERE user_id = #{userId} ORDER BY start_time DESC")
    List<ConsultSession> getConsultSessionsByUserId(Long userId);

    /**
     * 获取医生的所有问诊记录
     * @param doctorId 医生ID
     * @return 会话列表
     */
    @Select("SELECT * FROM consult_session WHERE doctor_id = #{doctorId} ORDER BY start_time DESC")
    List<ConsultSession> getConsultSessionsByDoctorId(Long doctorId);

    /**
     * 获取某医生的正在进行中的问诊
     * @param doctorId 医生ID
     * @return 会话实体
     */
    @Select("SELECT * FROM consult_session WHERE doctor_id = #{doctorId} AND status = 'ONGOING' LIMIT 1")
    ConsultSession getOngoingSessionByDoctorId(Long doctorId);

    /**
     * 更新问诊状态和结论
     * @param session 问诊会话实体
     * @return 更新记录数
     */
    @Update("UPDATE consult_session SET status = #{status}, conclusion = #{conclusion} WHERE id = #{id}")
    int updateConsultSessionStatusAndConclusion(ConsultSession session);

    /**
     * 插入一条问诊聊天消息
     * @param message 消息实体
     * @return 插入结果
     */
    @Insert("INSERT INTO consult_message (session_id, sender_id, sender_type, message, timestamp) " +
            "VALUES (#{sessionId}, #{senderId}, #{senderType}, #{message}, #{timestamp})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertConsultMessage(ConsultSession message);

    /**
     * 根据会话ID获取所有聊天消息
     * @param sessionId 会话ID
     * @return 消息列表
     */
    @Select("SELECT * FROM consult_message WHERE session_id = #{sessionId} ORDER BY send_time ASC")
    List<ConsultSession> getMessagesBySessionId(Long sessionId);

    /**
     * 删除某个问诊会话（及其相关消息）
     * 注意：该方法只删除会话，消息应通过外键或额外逻辑处理
     * @param sessionId 会话ID
     * @return 删除行数
     */
    @Delete("DELETE FROM consult_session WHERE id = #{sessionId}")
    int deleteConsultSession(Long sessionId);

    /**
     * 删除某个会话的所有聊天消息
     * @param sessionId 会话ID
     * @return 删除行数
     */
    @Delete("DELETE FROM consult_message WHERE session_id = #{sessionId}")
    int deleteMessagesBySessionId(Long sessionId);
}