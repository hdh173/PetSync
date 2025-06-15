package com.coursework.petsync.project.Controller;

import com.coursework.petsync.project.DTO.Request.ResetPasswordRequest;
import com.coursework.petsync.project.DTO.Request.UpdatePasswordRequest;
import com.coursework.petsync.project.DTO.Request.UserUpdateRequest;
import com.coursework.petsync.project.DTO.Response.PetsyncResponse;
import com.coursework.petsync.project.DTO.Response.ProfileResponse;
import com.coursework.petsync.project.DTO.entity.User;
import com.coursework.petsync.project.Service.UserService;
import com.coursework.petsync.utils.EmailUtil;
import com.coursework.petsync.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author HDH
 * @version 1.0
 */
@RestController
@RequestMapping("/profile")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PutMapping("/{id}/update")
    public PetsyncResponse<?> updateUserProfile(@PathVariable Long id,
                                                @ModelAttribute UserUpdateRequest request) {
        String avatar = null;
        if(request.getAvatarUrl() != null){
            try {
                avatar = userService.uploadAvatar(id,
                        request.getAvatarUrl());

            } catch (IOException e) {
                return PetsyncResponse.status(0, "头像文件存储错误", null);
            }
        }
        User user = new User(); // 将 DTO 转换为实体
        user.setId(id);
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname());
        user.setAvatarUrl(avatar);
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setSignature(request.getSignature());
        // 更新用户并返回最新数据
        user = userService.updateUserProfile(user);

        if (user != null) {
            return PetsyncResponse.success(null);
        } else {
            return PetsyncResponse.status(0, "用户信息更新失败", null);
        }
    }

    @PostMapping("/{id}")
    public PetsyncResponse<?> getCurrentUserProfile(@PathVariable Long id) {
        ProfileResponse response = userService.getUserProfile(id);

        if (response != null) {
            return PetsyncResponse.success(response);
        } else {
            return PetsyncResponse.status(400, "用户未找到", null);
        }
    }

    @PostMapping("/{id}/reset-password")
    public PetsyncResponse<?> resetPassword(@RequestBody ResetPasswordRequest request, @PathVariable String id) {
        boolean isValid = emailUtil.verifyEmailCode(request.getEmail(), request.getVerificationCode());
        if (!isValid) {
            return PetsyncResponse.failure(404, "验证码无效或已过期");
        }

        boolean success = userService.updatePassword(request.getEmail(), request.getNewPassword());

        if (success) {
            return PetsyncResponse.success("密码重置成功");
        } else {
            return PetsyncResponse.failure(0 , "密码重置失败");
        }
    }

    @PostMapping("/{id}/update-password")
    public PetsyncResponse<?> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest, @PathVariable String id){
        //System.out.println(id);
        boolean isValid = userService.updatePassword(id, updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword());
        if(isValid == true){
            return PetsyncResponse.success("密码修改成功");
        } else {
            return PetsyncResponse.failure(0, "密码修改失败");
        }
    }

}
