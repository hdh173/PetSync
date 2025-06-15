package com.coursework.petsync.project.Controller;

import com.coursework.petsync.project.DTO.Response.PetsyncResponse;
import com.coursework.petsync.project.DTO.entity.PetEvent;
import com.coursework.petsync.project.Service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author HDH
 * @version 1.0
 */
@RestController
@RequestMapping("/event")
@Tag(name = "事件簿模块", description = "提供事件增删改查接口")
public class EventController {
    @Autowired
    EventService eventService;

    @PostMapping("/add")
    @Operation(summary = "新建事件")
    public PetsyncResponse<Long> addPetEvent(@RequestBody PetEvent request) {
        Long eventId = eventService.addPetEvent(request);
        return PetsyncResponse.success(eventId);
    }

    @PutMapping("/{eventId}")
    @Operation(summary = "修改事件")
    public PetsyncResponse<Void> updatePetEvent(@PathVariable Long eventId, @RequestBody PetEvent request) {
        eventService.updatePetEvent(eventId, request);
        return PetsyncResponse.success(null);
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "删除事件")
    public PetsyncResponse<Void> deletePetEvent(@PathVariable Long eventId) {
        eventService.deletePetEvent(eventId);
        return PetsyncResponse.success(null);
    }

    @GetMapping("/{uid}/{pid}/event")
    @Operation(summary = "获取事件列表")
    public PetsyncResponse<List<PetEvent>> listPetEvents(
            @PathVariable Long uid,
            @PathVariable Long pid) {
        Long petId = pid == -1 ? null : pid;
        List<PetEvent> events = eventService.listPetEvents(uid, petId);
        return PetsyncResponse.success(events);
    }
}
