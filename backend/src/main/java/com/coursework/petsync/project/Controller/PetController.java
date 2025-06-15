package com.coursework.petsync.project.Controller;

import com.coursework.petsync.project.DTO.Response.PetsyncResponse;
import com.coursework.petsync.project.DTO.entity.Pet;
import com.coursework.petsync.project.Service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宠物信息管理控制器
 * 提供添加、修改、删除和查询接口
 */
@RestController
@RequestMapping("/pet")
@Tag(name = "宠物信息管理", description = "用户可管理自己添加的宠物档案")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping("/{uid}")
    @Operation(summary = "添加宠物", description = "添加一只新的宠物到当前用户名下")
    public PetsyncResponse<?> addPet(@PathVariable Long uid,
            @RequestBody Pet pet) {
        return petService.addPet(uid, pet)
                ? PetsyncResponse.success("添加成功")
                : PetsyncResponse.failure(400, "添加失败");
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改宠物信息", description = "更新指定宠物信息（仅限本人）")
    public PetsyncResponse<?> updatePet(@PathVariable Long id, @RequestBody Pet pet) {
        pet.setId(id);
        return petService.updatePet(id, pet)
                ? PetsyncResponse.success("修改成功")
                : PetsyncResponse.failure(400, "修改失败或权限不足");
    }

    @DeleteMapping("/{id}/{petId}")
    @Operation(summary = "删除宠物信息", description = "删除指定ID的宠物（仅限本人）")
    public PetsyncResponse<?> deletePet(@PathVariable Long id, @PathVariable Long petId) {
        return petService.deletePet(id, petId)
                ? PetsyncResponse.success("删除成功")
                : PetsyncResponse.failure(400, "删除失败或权限不足");
    }

    @PostMapping("/{id}/mine")
    @Operation(summary = "查询我的宠物", description = "获取当前登录用户下所有宠物信息")
    public PetsyncResponse<List<Pet>> getMyPets(@PathVariable Long id) {
        return PetsyncResponse.success(petService.getMyPets(id));
    }

//    @GetMapping("/all")
//    @Operation(summary = "查看所有宠物", description = "公开获取所有宠物档案（不含隐私信息）")
//    public PetsyncResponse<List<Pet>> getAllPets() {
//        return PetsyncResponse.success(petService.getAllPets());
//    }
}