package com.coursework.petsync.utils;

/**
 * @author HDH
 * @version 1.0
 */

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 * 负责生成、解析和校验token
 */
/**
 * Jwt 工具类，负责生成与验证 Token
 * 输入：用户 ID、角色
 * 输出：JWT Token 字符串或解析结果
 * 注意事项：
 * - 使用 JJWT 0.12.6 新 API。
 * - HS256 签名算法，密钥长度需满足安全要求。
 */
@Component
@Getter
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}") // 单位：毫秒
    private long expirationTime;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationTime;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 JWT Token
     *
     * @param userId 用户唯一标识
     * @param role   用户角色
     * @return Token 字符串
     */
    public String generateToken(Long userId, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 验证 Token 并返回 Claims
     *
     * @param token JWT 字符串
     * @return 解析后的 Claims 对象
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 判断 Token 是否过期
     *
     * @param token JWT 字符串
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        Date expiration = parseToken(token).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 从 Token 中获取用户 ID
     *
     * @param token JWT 字符串
     * @return 用户 ID
     */
    public Long getUserId(String token) {
        return Long.valueOf(parseToken(token).getSubject());
    }

    /**
     * 从 Token 中获取角色
     *
     * @param token JWT 字符串
     * @return 角色名称
     */
    public String getUserRole(String token) {
        return parseToken(token).get("role", String.class);
    }

    /**
     * 生成刷新令牌（Refresh Token）
     *
     * @param userId 用户唯一标识
     * @return Refresh Token 字符串
     */
    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpirationTime);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}
