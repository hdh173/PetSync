package com.coursework.petsync.project.Mapper;

import com.coursework.petsync.project.DTO.entity.DoctorApplication;
import com.coursework.petsync.project.DTO.entity.DoctorOnline;
import com.coursework.petsync.project.DTO.entity.DoctorProfile;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DoctorApplication 表数据库访问层
 */

@Mapper
public interface DoctorMapper {

    /**
     * 查询用户是否存在待审核的医生认证申请
     * @param userId 用户ID
     * @return 未审核记录数量
     */
    @Select("SELECT COUNT(*) FROM doctor_application WHERE user_id = #{userId} AND status = 'PENDING'")
    int countPendingByUserId(@Param("userId") Long userId);

    /**
     * 插入新的医生认证申请记录
     * @param userId 用户ID
     * @param realName 真实姓名
     * @param licenseNumber 执业证编号
     * @param idCardNumber 身份证号
     * @param hospitalName 所在医院
     * @param certificateUrl 执业证书图片地址
     * @param status 审核状态
     * @param createTime 创建时间
     * @param updateTime 更新时间
     * @return 插入成功的记录数
     */
    @Insert("INSERT INTO doctor_application " +
            "(user_id, real_name, license_number, id_card_number, hospital_name, certificate_url, status, create_time, update_time) " +
            "VALUES (#{userId}, #{realName}, #{licenseNumber}, #{idCardNumber}, #{hospitalName}, #{certificateUrl}, #{status}, #{createTime}, #{updateTime})")
    int insertDoctorApplication(@Param("userId") Long userId,
                                @Param("realName") String realName,
                                @Param("licenseNumber") String licenseNumber,
                                @Param("idCardNumber") String idCardNumber,
                                @Param("hospitalName") String hospitalName,
                                @Param("certificateUrl") String certificateUrl,
                                @Param("status") String status,
                                @Param("createTime") LocalDateTime createTime,
                                @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据用户ID获取医生展示信息
     * @param userId 用户ID
     * @return 医生展示信息
     */
    @Select("SELECT * FROM doctor_profile WHERE user_id = #{userId}")
    DoctorProfile getByUserId(@Param("userId") Long userId);

    /**
     * 插入医生展示信息
     * @param doctorProfile 医生展示实体
     * @return 插入成功记录数
     */
    @Insert("INSERT INTO doctor_profile (user_id, specialty, title, experience, achievements, profile_picture_url, create_time, update_time) " +
            "VALUES (#{userId}, #{specialty}, #{title}, #{experience}, #{achievements}, #{profilePictureUrl}, #{createTime}, #{updateTime})")
    int insertProfile(DoctorProfile doctorProfile);

    /**
     * 更新医生展示信息
     * @param doctorProfile 医生展示实体
     * @return 更新成功记录数
     */
    @Update("UPDATE doctor_profile SET " +
            "specialty = #{specialty}, " +
            "title = #{title}, " +
            "experience = #{experience}, " +
            "achievements = #{achievements}, " +
            "profile_picture_url = #{profilePictureUrl}, " +
            "update_time = #{updateTime} " +
            "WHERE user_id = #{userId}")
    int updateProfile(DoctorProfile doctorProfile);

    /**
     * 根据 ID 查询医生申请
     * @param id 申请 ID
     * @return DoctorApplication 对象
     */
    @Select("SELECT * FROM doctor_application WHERE id = #{id}")
    DoctorApplication getById(@Param("id") Long id);

    /**
     * 更新申请状态（审核操作）
     * @param application 包含 ID、status、remark、update_time
     * @return 更新成功的记录数
     */
    @Update("UPDATE doctor_application SET status = #{status}, remark = #{remark}, update_time = #{updateTime} WHERE id = #{id}")
    int updateStatus(DoctorApplication application);

    /**
     * 插入新申请（如果有前端发起申请）
     * @param application DoctorApplication 实例
     * @return 插入成功的记录数
     */
    @Insert("INSERT INTO doctor_application(user_id, real_name, id_number, certificate_url, status, remark, create_time, update_time) " +
            "VALUES(#{userId}, #{realName}, #{idNumber}, #{certificateUrl}, #{status}, #{remark}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DoctorApplication application);

    /**
     * 获取用户的最新申请记录（可选方法）
     * @param userId 用户 ID
     * @return 最新申请记录
     */
    @Select("SELECT * FROM doctor_application WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT 1")
    DoctorApplication getLatestByUserId(@Param("userId") Long userId);

    /**
     * 查询所有待审核的医生申请记录
     *
     * @return 待审核医生申请列表
     */
    @Select("SELECT * FROM doctor_application WHERE status = 'PENDING'")
    List<DoctorApplication> getPendingApplications();

    /**
     * 根据医生 ID 列表获取医生资料
     * @param ids 医生 ID 列表
     * @return 医生资料列表
     */
    @Select({
            "<script>",
            "SELECT * FROM doctor_profile",
            "WHERE doctor_id IN",
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    List<DoctorOnline> getDoctorsByIds(@Param("ids") List<Long> ids);
}