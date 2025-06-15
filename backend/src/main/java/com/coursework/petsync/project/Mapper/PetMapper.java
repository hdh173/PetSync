package com.coursework.petsync.project.Mapper;

import com.coursework.petsync.project.DTO.entity.Pet;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 宠物数据库操作接口
 */
@Mapper
public interface PetMapper {

    @Insert("INSERT INTO pet (user_id, name, type, breed, gender, birthday, avatar_url, create_time, update_time) " +
            "VALUES (#{userId}, #{name}, #{type}, #{breed}, #{gender}, #{birthDay}, #{avatarUrl}, #{createdAt}, #{updatedAt})")
    int insert(Pet pet);

    @Update("UPDATE pet SET name=#{name}, type=#{type}, breed=#{breed}, gender=#{gender}, birthday=#{birthDay}, " +
            "avatar_url=#{avatarUrl}, update_time=#{updatedAt} WHERE id=#{id}")
    int update(Pet pet);

    @Delete("DELETE FROM pet WHERE id=#{id}")
    int delete(Long id);

    @Select("SELECT * FROM pet WHERE user_id=#{userId}")
    List<Pet> findByUserId(Long userId);

    @Select("SELECT * FROM pet")
    List<Pet> findAll();

    @Select("SELECT user_id FROM pet WHERE id=#{petId}")
    Long getUserIdByPetId(Long petId);
}
