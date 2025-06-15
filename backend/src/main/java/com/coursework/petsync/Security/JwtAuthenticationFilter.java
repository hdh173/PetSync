package com.coursework.petsync.Security;

/**
 * @author HDH
 * @version 1.0
 */

import com.coursework.petsync.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 身份验证过滤器
 * 输入：包含 JWT Token 的请求头
 * 输出：设置用户上下文到 Spring SecurityContext
 * 注意事项：
 * - 每次请求只执行一次。
 * - 忽略 /auth/** 路径等无需认证的请求（可在 SecurityConfig 控制）。
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            if (!jwtUtil.isTokenExpired(token)) {
                Long userId = jwtUtil.getUserId(token);
                String role = jwtUtil.getUserRole(token);

                UserDetailsImpl userDetails = userDetailsService.loadUserById(userId);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // 可以记录日志，但不要抛出异常阻断请求
            logger.warn("JWT 验证失败: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}