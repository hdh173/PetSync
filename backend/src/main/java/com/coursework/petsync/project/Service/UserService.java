package com.coursework.petsync.project.Service;

import com.coursework.petsync.project.DTO.Response.ProfileResponse;
import com.coursework.petsync.project.DTO.entity.User;
import com.coursework.petsync.project.Mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author HDH
 * @version 1.0
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Value("${avatar.upload-dir}")
    private String uploadDir;

    @Value("${server.url}")
    private String serverUrl;
    public String uploadAvatar(Long userId, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID() + ext;

        File dest = new File(uploadDir + File.separator + filename);
        file.transferTo(dest);
        // 拼接访问 URL
        return serverUrl + "/avatar/" + filename;
    }

    public User updateUserProfile(User user) {
        int updatedRows = userMapper.updateUserProfile(user);
        if (updatedRows > 0) {
            return userMapper.findByID(user.getId());
        } else {
            return null; // 更新失败，可能是用户id不存在
        }
    }

    public ProfileResponse getUserProfile(Long id) {
        User user = userMapper.findById(id);
        if (user == null)
            return null;
        return new ProfileResponse(
                user.getUsername(), user.getNickname(), user.getAvatarUrl(),
                user.getPhone(), user.getGender(),user.getSignature()
        );
    }

    public boolean updatePassword(String email, String newPassword) {
        int updatedRows = userMapper.updatePassword(newPassword, email);
        return updatedRows > 0;
    }

    public boolean updatePassword(String id, String oldPassword, String newPassword) {
        String old = userMapper.getUserPassword(id);
        //System.out.println(old);
        if(oldPassword == null || !oldPassword.equals(old)){
            return false;
        }

        int updatedRows = userMapper.updatePasswordByID(newPassword, id);
        return updatedRows > 0;
    }
}
