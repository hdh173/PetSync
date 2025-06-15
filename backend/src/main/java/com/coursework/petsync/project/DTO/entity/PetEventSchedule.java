package com.coursework.petsync.project.DTO.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetEventSchedule {
    private Long id;
    private Long eventId;
    private Date eventTime;
    private String repeatType; // none/daily/weekly/monthly/yearly/custom
    private Integer repeatCount;
    private String repeatDays; // 如"2,4"表示周二和周四
    private Boolean isActive;
    private Date nextTriggerTime;
}