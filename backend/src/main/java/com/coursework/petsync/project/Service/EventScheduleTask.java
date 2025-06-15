package com.coursework.petsync.project.Service;

import com.coursework.petsync.project.DTO.entity.PetEventSchedule;
import com.coursework.petsync.project.Mapper.PetEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventScheduleTask {
    private final PetEventMapper petEventMapper;
    private final EventService eventService;

    @Scheduled(cron = "0 0 0 * * ?") // 每天凌晨执行
    public void checkEventSchedules() {
        // 1. 获取所有活跃的定时
        List<PetEventSchedule> schedules = petEventMapper.selectActiveSchedules();
        Date now = new Date();

        for (PetEventSchedule schedule : schedules) {
            // 2. 检查是否需要触发
            if (schedule.getNextTriggerTime() != null &&
                    schedule.getNextTriggerTime().before(now)) {

                // 3. 处理重复次数
                if (schedule.getRepeatCount() != null) {
                    if (schedule.getRepeatCount() <= 0) {
                        // 次数耗尽，删除定时
                        petEventMapper.deleteSchedule(schedule.getId());

                        // 检查事件是否还有其他定时
                        int count = petEventMapper.countSchedulesByEventId(schedule.getEventId());
                        if (count == 0) {
                            petEventMapper.deleteEvent(schedule.getEventId());
                        }
                        continue;
                    }

                    // 减少剩余次数
                    schedule.setRepeatCount(schedule.getRepeatCount() - 1);
                }

                // 4. 计算下次触发时间
                if (!"none".equals(schedule.getRepeatType())) {
                    eventService.calculateNextTriggerTime(schedule);
                    petEventMapper.updateSchedule(schedule);
                } else {
                    // 一次性事件，触发后删除
                    petEventMapper.deleteSchedule(schedule.getId());

                    // 检查事件是否还有其他定时
                    int count = petEventMapper.countSchedulesByEventId(schedule.getEventId());
                    if (count == 0) {
                        petEventMapper.deleteEvent(schedule.getEventId());
                    }
                }

                // 5. 这里可以添加触发事件后的处理逻辑，如发送提醒等
                // triggerEventNotification(schedule);
            }
        }
    }
}