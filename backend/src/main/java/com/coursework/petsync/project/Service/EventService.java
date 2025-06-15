package com.coursework.petsync.project.Service;

import com.coursework.petsync.project.DTO.entity.PetEvent;
import com.coursework.petsync.project.DTO.entity.PetEventSchedule;
import com.coursework.petsync.project.Mapper.PetEventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author HDH
 * @version 1.0
 */
@Service
public class EventService {
    @Autowired
    PetEventMapper petEventMapper;

    public Long addPetEvent(PetEvent event) {
        petEventMapper.insertEvent(event);
        Long eventId = event.getId();

        if (event.getSchedules() != null) {
            for (PetEventSchedule schedule : event.getSchedules()) {
                schedule.setEventId(eventId);
                calculateNextTriggerTime(schedule);
                petEventMapper.insertSchedule(schedule);
            }
        }

        return eventId;
    }

    public void updatePetEvent(Long eventId, PetEvent event) {
        event.setId(eventId);
        petEventMapper.updateEvent(event);

        // 先删除原有定时
        petEventMapper.deleteSchedulesByEventId(eventId);

        // 添加新定时
        if (event.getSchedules() != null) {
            for (PetEventSchedule schedule : event.getSchedules()) {
                schedule.setEventId(eventId);
                calculateNextTriggerTime(schedule);
                petEventMapper.insertSchedule(schedule);
            }
        }
    }

    public void deletePetEvent(Long eventId) {
        petEventMapper.deleteSchedulesByEventId(eventId);
        petEventMapper.deleteEvent(eventId);
    }

    public List<PetEvent> listPetEvents(Long userId, Long petId) {
        List<PetEvent> events = petEventMapper.selectEventsByUserAndPet(userId, petId);

        for (PetEvent event : events) {
            List<PetEventSchedule> schedules = petEventMapper.selectSchedulesByEventId(event.getId());
            event.setSchedules(schedules);
        }

        return events;
    }

    public void calculateNextTriggerTime(PetEventSchedule schedule) {
        if ("none".equals(schedule.getRepeatType())) {
            schedule.setNextTriggerTime(schedule.getEventTime());
            return;
        }

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(schedule.getEventTime());

        // 如果初始时间在未来，直接使用
        if (schedule.getEventTime().after(now)) {
            schedule.setNextTriggerTime(schedule.getEventTime());
            return;
        }

        switch (schedule.getRepeatType()) {
            case "daily":
                while (cal.getTime().before(now)) {
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                }
                break;

            case "weekly":
                if (schedule.getRepeatDays() != null && !schedule.getRepeatDays().isEmpty()) {
                    // 自定义每周几天的情况，如"2,4"表示周二和周四
                    String[] days = schedule.getRepeatDays().split(",");
                    boolean found = false;

                    for (String dayStr : days) {
                        int targetDay = Integer.parseInt(dayStr.trim());
                        Calendar tempCal = (Calendar) cal.clone();

                        // 设置到最近的指定星期几
                        tempCal.set(Calendar.DAY_OF_WEEK, targetDay);
                        if (tempCal.getTime().after(now)) {
                            cal = tempCal;
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        // 如果本周没有找到，跳到下周的第一个指定日
                        int firstDay = Integer.parseInt(days[0].trim());
                        cal.add(Calendar.WEEK_OF_YEAR, 1);
                        cal.set(Calendar.DAY_OF_WEEK, firstDay);
                    }
                } else {
                    // 普通每周重复
                    while (cal.getTime().before(now)) {
                        cal.add(Calendar.WEEK_OF_YEAR, 1);
                    }
                }
                break;

            case "monthly":
                if (schedule.getRepeatDays() != null && !schedule.getRepeatDays().isEmpty()) {
                    // 自定义每月几号的情况，如"5,15"表示每月5号和15号
                    String[] days = schedule.getRepeatDays().split(",");
                    boolean found = false;

                    for (String dayStr : days) {
                        int targetDay = Integer.parseInt(dayStr.trim());
                        Calendar tempCal = (Calendar) cal.clone();

                        // 处理超过当月天数的情况
                        int maxDay = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int dayToSet = Math.min(targetDay, maxDay);

                        tempCal.set(Calendar.DAY_OF_MONTH, dayToSet);
                        if (tempCal.getTime().after(now)) {
                            cal = tempCal;
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        // 如果本月没有找到，跳到下月的第一个指定日
                        int firstDay = Integer.parseInt(days[0].trim());
                        cal.add(Calendar.MONTH, 1);
                        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int dayToSet = Math.min(firstDay, maxDay);
                        cal.set(Calendar.DAY_OF_MONTH, dayToSet);
                    }
                } else {
                    // 普通每月重复(同一天)
                    while (cal.getTime().before(now)) {
                        cal.add(Calendar.MONTH, 1);
                    }
                }
                break;

            case "yearly":
                // 每年同一天
                while (cal.getTime().before(now)) {
                    cal.add(Calendar.YEAR, 1);
                }
                break;

            case "custom":
                // 自定义复杂规则，这里简化处理为每天
                while (cal.getTime().before(now)) {
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                }
                break;

            default:
                schedule.setNextTriggerTime(schedule.getEventTime());
                return;
        }

        // 更新重复次数(如果有设置)
        if (schedule.getRepeatCount() != null && schedule.getRepeatCount() > 0) {
            schedule.setRepeatCount(schedule.getRepeatCount() - 1);
        }

        schedule.setNextTriggerTime(cal.getTime());
    }
}
