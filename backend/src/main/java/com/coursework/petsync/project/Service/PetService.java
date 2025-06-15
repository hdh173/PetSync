package com.coursework.petsync.project.Service;

import com.coursework.petsync.Security.UserDetailsImpl;
import com.coursework.petsync.project.DTO.entity.Pet;
import com.coursework.petsync.project.Mapper.PetMapper;
import com.coursework.petsync.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author HDH
 * @version 1.0
 */
@Service
public class PetService {
    @Autowired
    private PetMapper petMapper;

    public boolean addPet(Long uid, Pet pet) {
        Long userId = uid;
        pet.setUserId(userId);
        pet.setCreatedTime(LocalDateTime.now());
        pet.setUpdatedTime(LocalDateTime.now());
        return petMapper.insert(pet) > 0;
    }

    public boolean updatePet(Long id, Pet pet) {
        Long ownerId = petMapper.getUserIdByPetId(pet.getId());
        if (ownerId == null || !ownerId.equals(id)) {
            return false;
        }
        pet.setUpdatedTime(LocalDateTime.now());
        return petMapper.update(pet) > 0;
    }

    public boolean deletePet(Long Id, Long petId) {
        Long ownerId = petMapper.getUserIdByPetId(petId);
        if (ownerId == null || !ownerId.equals(Id)) {
            return false;
        }
        return petMapper.delete(petId) > 0;
    }

    public List<Pet> getMyPets(Long id) {
        Long userId = id;
        return petMapper.findByUserId(userId);
    }

    public List<Pet> getAllPets() {
        return petMapper.findAll();
    }
}
