package com.coursework.petsync.project.Service;

/**
 * @author HDH
 * @version 1.0
 */

import com.coursework.petsync.project.DTO.Request.DoctorProfileRequest;
import com.coursework.petsync.project.DTO.Response.DoctorApplicationResponse;
import com.coursework.petsync.project.DTO.Response.DoctorProfileResponse;
import com.coursework.petsync.project.DTO.entity.DoctorApplication;
import com.coursework.petsync.project.DTO.entity.DoctorProfile;
import com.coursework.petsync.project.Mapper.DoctorMapper;
import com.coursework.petsync.utils.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 医生身份认证相关服务类
 * 处理医生申请身份认证的业务逻辑
 */
@Service
public class DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String ONLINE_DOCTOR_SET_KEY = "online_doctor_set";
    /**
     * 检查用户是否已有待审核申请
     * @param userId 用户ID
     * @return 是否存在PENDING状态的申请
     */
    public boolean hasPendingApplication(Long userId) {
        return doctorMapper.countPendingByUserId(userId) > 0;
    }

    /**
     * 提交医生身份认证申请
     * @param userId 用户ID
     * @param realName 真实姓名
     * @param licenseNumber 执业证书编号
     * @param idCardNumber 身份证号
     * @param hospitalName 医院名称
     * @param certificateUrl 资质证书图片链接
     * @return 是否插入成功
     */
    public boolean submitApplication(Long userId, String realName, String licenseNumber,
                                     String idCardNumber, String hospitalName, String certificateUrl) {
        int rows = doctorMapper.insertDoctorApplication(
                userId, realName, licenseNumber, idCardNumber, hospitalName, certificateUrl,
                "PENDING", LocalDateTime.now(), LocalDateTime.now()
        );
        return rows > 0;
    }

    public void incrementVisitCount(Long doctorId) {
        String key = "doctor:visit:" + doctorId;
        redisTemplate.opsForValue().increment(key);
    }


    public boolean updateDoctorProfile(Long userId, DoctorProfileRequest request) {
        DoctorProfile existing = doctorMapper.getByUserId(userId);
        DoctorProfile profile = new DoctorProfile();
        profile.setUserId(userId);
        profile.setSpecialty(request.getSpecialty());
        profile.setTitle(request.getTitle());
        profile.setExperience(request.getExperience());
        profile.setAchievements(request.getAchievements());
        profile.setProfilePictureUrl(request.getProfilePictureUrl());

        if (existing == null) {
            return doctorMapper.insertProfile(profile) > 0;
        } else {
            return doctorMapper.updateProfile(profile) > 0;
        }
    }


    public DoctorProfileResponse getDoctorProfile(Long userId) {
        DoctorProfile profile = doctorMapper.getByUserId(userId);
        if (profile == null) return null;

        DoctorProfileResponse response = new DoctorProfileResponse();
        response.setUserId(userId);
        response.setSpecialty(profile.getSpecialty());
        response.setTitle(profile.getTitle());
        response.setExperience(profile.getExperience());
        response.setAchievements(profile.getAchievements());
        response.setProfilePictureUrl(profile.getProfilePictureUrl());

        String ratingKey = "doctor:rating:" + userId;
        String visitKey = "doctor:visit:" + userId;

        Object ratingValue = redisTemplate.opsForValue().get(ratingKey);
        Object visitValue = redisTemplate.opsForValue().get(visitKey);

        response.setRating(ratingValue == null ? 0.0 : Double.parseDouble(ratingValue.toString()));
        response.setVisitCount(visitValue == null ? 0 : Long.parseLong(visitValue.toString()));

        // 自增访问量并设置过期时间（1天 = 1440 分钟）
        redisUtil.incrementWithExpire(visitKey, 60 * 24);


        return response;
    }

    /**
     * 审核医生认证申请
     *
     * @param applicationId 申请记录 ID
     * @param approve 是否通过审核（true 为通过，false 为拒绝）
     * @param remark 审核备注，可为空
     * @return 是否审核成功（成功审核且状态更新成功）
     */
    public boolean reviewApplication(Long applicationId, boolean approve, String remark) {
        DoctorApplication application = doctorMapper.getById(applicationId);
        if (application == null || application.getStatus() != "PENDING") {
            // 不存在或已审核过
            return false;
        }

        // 更新申请状态
        application.setStatus(approve ? "APPROVED":"REJECTED"); // 1=通过，2=拒绝
        application.setRemark(remark);
        application.setUpdateTime(LocalDateTime.now());

        int updateResult = doctorMapper.updateStatus(application);

        // 如果审核通过，自动在 doctor_profile 表创建初始医生信息（后续医生可完善）
        if (approve) {
            DoctorProfile profile = new DoctorProfile();
            profile.setUserId(application.getUserId());
            profile.setSpecialty(""); // 初始为空
            profile.setTitle("");
            profile.setExperience("");
            profile.setAchievements("");
            profile.setProfilePictureUrl("");
            profile.setCreateTime(LocalDateTime.now());
            profile.setUpdateTime((LocalDateTime.now()));
            doctorMapper.insertProfile(profile);
        }

        return updateResult > 0;
    }

    public List<DoctorApplicationResponse> getPendingApplications() {
        List<DoctorApplication> applications = doctorMapper.getPendingApplications();
        return applications.stream().map(app -> {
            DoctorApplicationResponse response = new DoctorApplicationResponse();
            response.setId(app.getId());
            response.setUserId(app.getUserId());
            response.setRealName(app.getRealName());
            response.setLicenseNumber(app.getLicenseNumber());
            response.setSubmittedAt(app.getCreateTime());
            response.setStatus(app.getStatus());
            return response;
        }).collect(Collectors.toList());
    }
}
