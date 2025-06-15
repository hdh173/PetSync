package com.coursework.petsync.Security;

/**
 * @author HDH
 * @version 1.0
 */

import com.coursework.petsync.project.DTO.entity.User;
import com.coursework.petsync.project.Mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户详情服务类，供 Spring Security 获取用户信息
 * 输入：用户名或 ID
 * 输出：UserDetailsImpl
 * 注意事项：
 * - 需实现 loadUserByUsername 和自定义 loadUserById
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 根据邮箱加载用户
     *
     * @param email 用户邮箱
     * @return UserDetailsImpl
     * @throws UsernameNotFoundException 如果用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("未找到用户：" + email);
        }
        return UserDetailsImpl.build(user);
    }

    /**
     * 根据用户 ID 加载用户（JWT 用途）
     *
     * @param userId 用户 ID
     * @return UserDetailsImpl
     */
    public UserDetailsImpl loadUserById(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new UsernameNotFoundException("未找到用户 ID：" + userId);
        }
        return UserDetailsImpl.build(user);
    }
}