package com.coursework.petsync.project.Mapper;

import com.coursework.petsync.project.DTO.entity.ConsultSession;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConsultSessionMapper {

    @Insert("INSERT INTO consult_session(user_id, doctor_id, pet_id, status, start_time, create_time, update_time) " +
            " VALUES (#{userId}, #{doctorId}, #{petId}, #{status}, #{startTime}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ConsultSession session);

    @Select("SELECT * FROM consult_session WHERE id = #{id}")
    ConsultSession findById(Long id);

    @Update("UPDATE consult_session " +
            "SET status = #{status}, start_time = #{startTime}, end_time = #{endTime}, update_time = NOW() " +
            "WHERE id = #{id}")
    int updateStatus(ConsultSession session);

    @Select("SELECT * FROM consult_session " +
            "WHERE user_id = #{userId} OR doctor_id = #{userId} " +
            "ORDER BY create_time DESC")
    List<ConsultSession> findByUser(Long userId);
}
