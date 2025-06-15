package com.coursework.petsync.project.Mapper;

import com.coursework.petsync.project.DTO.entity.PetEvent;
import com.coursework.petsync.project.DTO.entity.PetEventSchedule;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author HDH
 * @version 1.0
 */
@Mapper
public interface PetEventMapper {
    @Insert("INSERT INTO pet_event(pet_id, user_id, event_type, description, remind_type, create_time) " +
            "VALUES(#{petId}, #{userId}, #{eventType}, #{description}, #{remindType}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertEvent(PetEvent event);

    @Update("UPDATE pet_event SET " +
            "pet_id = #{petId}, " +
            "user_id = #{userId}, " +
            "event_type = #{eventType}, " +
            "description = #{description}, " +
            "remind_type = #{remindType} " +
            "WHERE id = #{id}")
    int updateEvent(PetEvent event);

    @Delete("DELETE FROM pet_event WHERE id = #{eventId}")
    int deleteEvent(Long eventId);

    @Select("SELECT * FROM pet_event WHERE id = #{eventId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "schedules", column = "id",
                    many = @Many(select = "selectSchedulesByEventId"))
    })
    PetEvent selectEventById(Long eventId);

    @Select("<script>" +
            "SELECT * FROM pet_event WHERE user_id = #{userId} " +
            "<if test='petId != null'> AND pet_id = #{petId} </if>" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "schedules", column = "id",
                    many = @Many(select = "selectSchedulesByEventId"))
    })
    List<PetEvent> selectEventsByUserAndPet(@Param("userId") Long userId, @Param("petId") Long petId);

    // 定时相关
    @Insert("INSERT INTO pet_event_schedule(event_id, event_time, repeat_type, repeat_count, repeat_days, is_active, next_trigger_time) " +
            "VALUES(#{eventId}, #{eventTime}, #{repeatType}, #{repeatCount}, #{repeatDays}, #{isActive}, #{nextTriggerTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSchedule(PetEventSchedule schedule);

    @Update("UPDATE pet_event_schedule SET " +
            "event_time = #{eventTime}, " +
            "repeat_type = #{repeatType}, " +
            "repeat_count = #{repeatCount}, " +
            "repeat_days = #{repeatDays}, " +
            "is_active = #{isActive}, " +
            "next_trigger_time = #{nextTriggerTime} " +
            "WHERE id = #{id}")
    int updateSchedule(PetEventSchedule schedule);

    @Delete("DELETE FROM pet_event_schedule WHERE id = #{scheduleId}")
    int deleteSchedule(Long scheduleId);

    @Delete("DELETE FROM pet_event_schedule WHERE event_id = #{eventId}")
    int deleteSchedulesByEventId(Long eventId);

    @Select("SELECT * FROM pet_event_schedule WHERE event_id = #{eventId}")
    List<PetEventSchedule> selectSchedulesByEventId(Long eventId);

    @Select("SELECT * FROM pet_event_schedule WHERE is_active = TRUE")
    List<PetEventSchedule> selectActiveSchedules();

    @Select("SELECT COUNT(*) FROM pet_event_schedule WHERE event_id = #{eventId}")
    int countSchedulesByEventId(Long eventId);
}