package com.coursework.petsync.project.Mapper;

import com.coursework.petsync.project.DTO.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author HDH
 * @version 1.0
 */
@Mapper
public interface UserMapper {
    /**
     * 通过用户ID查询用户
     * @param userId 用户ID
     * @return 用户信息
     */
    @Select("SELECT * FROM user WHERE id = #{userId}")
    User findById(@Param("userId") Long userId);

    /**
     * 通过邮箱查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(@Param("email") String email);

    /**
     * 注册用户
     * @param user 用户实体
     * @return 插入成功记录数
     */
    @Insert("INSERT INTO user (username, password, nickname, avatar_url, email, phone, gender, signature, create_time, update_time) " +
            "VALUES (#{username}, #{password}, #{nickname}, #{avatarUrl}, #{email}, #{phone}, #{gender}, #{signature}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    User registerUser(User user);

    /**
     * 通过用户名或邮箱查询用户（登录使用）
     * @param email 用户名或邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM user WHERE username = #{email} OR email = #{email}")
    User findByUsernameOrEmail(@Param("email") String email);

    /**
     * 更新用户信息（基本资料）
     * @param user 用户实体（包含需要更新字段）
     * @return 更新记录数
     */
    @Update("UPDATE user SET nickname = #{nickname}, avatar_url = #{avatarUrl}, gender = #{gender}, " +
            "signature = #{signature}, update_time = NOW() WHERE id = #{id}")
    int updateUserProfile(User user);

    /**
     * 通过ID查询用户信息（与 findById 类似，若需要保留，注意命名一致）
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findByID(@Param("id") Long id);

    /**
     * 通过邮箱或用户名更新密码
     * @param newPassword 新密码（加密后）
     * @param emailOrID 邮箱或用户名
     * @return 更新记录数
     */
    @Update("UPDATE user SET password = #{newPassword}, update_time = NOW() WHERE email = #{emailOrID} OR username = #{emailOrID}")
    int updatePassword(@Param("newPassword") String newPassword, @Param("emailOrID") String emailOrID);

    /**
     * 通过用户名（或邮箱）获取用户密码
     * @param id 用户名或邮箱
     * @return 加密密码
     */
    @Select("SELECT password FROM user WHERE email = #{id} OR username = #{id}")
    String getUserPassword(@Param("id") String id);

    /**
     * 通过ID更新密码
     * @param newPassword 新密码（加密后）
     * @param id 用户ID
     * @return 更新记录数
     */
    @Update("UPDATE user SET password = #{newPassword}, update_time = NOW() WHERE id = #{id}")
    int updatePasswordByID(@Param("newPassword") String newPassword, @Param("id") String id);

    /**
     * 获取用户所有角色名
     */
    @Select("SELECT r.name FROM role r " +
            "JOIN user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> findRolesByUserId(@Param("userId") Long userId);

}
