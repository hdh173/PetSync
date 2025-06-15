package com.coursework.petsync.project.Mapper;

import com.coursework.petsync.project.DTO.entity.DiagnosisRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DiagnosisRecordMapper {
    @Insert("INSERT INTO diagnosis_record(session_id, doctor_id, pet_id, content, suggestion, create_time, update_time) " +
            "VALUES (#{sessionId}, #{doctorId}, #{petId}, #{content}, #{suggestion}, NOW(), NOW()) ")
    int insert(DiagnosisRecord record);

    @Select("SELECT * FROM diagnosis_record WHERE session_id = #{sessionId}")
    DiagnosisRecord findBySessionId(Long sessionId);
}
