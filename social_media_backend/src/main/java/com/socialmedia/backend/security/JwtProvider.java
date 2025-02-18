package com.socialmedia.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private static final long EXPIRATION_TIME = 86400000; // 24h
    private static final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtConstant.SECRET_KEY));

    /**
     * Tạo JWT từ email người dùng.
     */
    public String generateJwt(String email) {
        return Jwts.builder()
                .setSubject(email) // Đặt email làm subject của token
                .setIssuedAt(new Date()) // Thời gian phát hành
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Hết hạn sau 24h
                .signWith(key, SignatureAlgorithm.HS256) // Ký với thuật toán HS256
                .compact(); // Xuất JWT thành chuỗi
    }

    /**
     * Lấy email từ JWT.
     */
    public String getEmailFromToken(String jwt) {
        try {
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            return claims.getSubject(); // Lấy email từ subject
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token đã hết hạn");
        } catch (JwtException e) {
            throw new RuntimeException("Token không hợp lệ");
        }
    }

    /**
     * Kiểm tra JWT có hợp lệ không.
     */
    public boolean validateToken(String jwt) {
        try {
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return true;
        } catch (JwtException e) {
            return false; // Token không hợp lệ hoặc hết hạn
        }
    }
}
