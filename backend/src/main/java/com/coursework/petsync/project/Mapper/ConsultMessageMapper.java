package com.coursework.petsync.project.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ConsultMessageMapper {

    @Insert("INSERT INTO consult_message(session_id, sender_id, sender_role, message_type, content, send_time, is_read) " +
            "VALUES (#{sessionId}, #{senderId}, #{senderRole}, #{messageType}, #{content}, NOW(), 0)")
    int insert(com.coursework.petsync.project.DTO.entity.ConsultMessage msg);

    @Select("SELECT * FROM consult_message WHERE session_id = #{sessionId} ORDER BY send_time ASC")
    java.util.List<com.coursework.petsync.project.DTO.entity.ConsultMessage> listBySession(Long sessionId);
}
