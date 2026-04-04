package com.example.foodchooser.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtils {

    // 【重要】随便敲一长串英文字符作为加密盐，越复杂越安全，千万别泄露
    private static final String SECRET_STRING = "Hal9770_Food_Chooser_Secret_Key_2026!@#$%";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());

    // Token 有效期：1小时（毫秒单位）
    private static final long EXPIRATION_TIME = 3600 * 1000;

    /**
     * 1. 生成 Token
     */
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 把用户名塞进去
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // 签名防伪
                .compact();
    }

    /**
     * 2. 验证 Token 并解析出用户名
     */
    public static String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // 如果没报错，说明没过期且没篡改，返回用户名
        } catch (Exception e) {
            return null; // 报错说明 Token 非法或过期
        }
    }
}

