package com.coursework.petsync.project.Service;

import com.coursework.petsync.project.DTO.entity.User;
import com.coursework.petsync.project.Mapper.UserMapper;
import com.coursework.petsync.utils.EmailUtil;
import com.coursework.petsync.utils.JwtUtil;
import com.coursework.petsync.utils.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author HDH
 * @version 1.0
 */
@Service
public class AuthService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${avatar.upload-dir}")
    private String uploadDir;
    @Value("${server.url}")
    private String serverUrl;

    public User registerUser(User user, String code) {
        // 校验密码
        if (!PasswordValidator.isValid(user.getPassword())) {
            return null; // 密码不符合要求
        }

        // 校验验证码是否正确
        boolean isVerified = emailUtil.verifyEmailCode(user.getEmail(), code);
        if (!isVerified) {
            return null; // 验证码错误
        }

        // 保存用户到数据库
        user = userMapper.registerUser(user);

        return user;
    }

    public User login(String usernameOrEmail, String password) {
        User user = userMapper.findByUsernameOrEmail(usernameOrEmail);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; // 登录失败
    }
}
