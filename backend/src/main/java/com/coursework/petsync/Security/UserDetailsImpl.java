package com.coursework.petsync.Security;

/**
 * @author HDH
 * @version 1.0
 */

import com.coursework.petsync.project.DTO.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security 所需的用户详情实现类
 * 输入：系统用户实体 User
 * 输出：用户权限与认证信息
 * 注意事项：
 * - 用于身份验证和授权过程中提供用户信息
 */
@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final String role;

    /**
     * 获取权限列表
     *
     * @return 权限集合（例如 ROLE_USER、ROLE_ADMIN）
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 可根据业务添加状态字段
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 可根据业务添加状态字段
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 可设置密码有效期
    }

    @Override
    public boolean isEnabled() {
        return true; // 可根据用户是否禁用判断
    }

    /**
     * 创建 UserDetailsImpl 实例
     *
     * @param user User 实体对象
     * @return UserDetailsImpl
     */
    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }
}