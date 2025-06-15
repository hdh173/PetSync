package com.coursework.petsync.project.DTO.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetEvent {
    private Long id;
    private Long petId;
    private Long userId;
    private String eventType;
    private String description;
    private String remindType; // none/app/sms/email/all
    private Date createTime;
    private List<PetEventSchedule> schedules;
}