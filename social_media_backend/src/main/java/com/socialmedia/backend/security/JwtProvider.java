package com.socialmedia.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtProvider {

    private static final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtConstant.SECRET_KEY));

    public String generateJwt(Authentication auth) {
        return Jwts.builder()
                .setIssuedAt(new Date()) // Thời gian phát hành JWT
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Hết hạn sau 1 ngày
                .claim("email", auth.getName()) // Thêm claim chứa email người dùng
                .signWith(key) // Ký với khóa bí mật
                .compact(); // Kết xuất JWT thành chuỗi
    }

    public String getEmailFromToken(String jwt) {
        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }

        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

        return claims.get("email", String.class);
    }
}
