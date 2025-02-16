package com.socialmedia.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private static final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtConstant.SECRET_KEY));

    public String generateJwt(String email) {
        return Jwts.builder()
                .setIssuedAt(new Date())  // Thời gian phát hành JWT
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Hết hạn sau 24h
                .claim("email", email) // Thêm claim chứa email
                .signWith(key) // Ký với khóa bí mật
                .compact(); // Xuất JWT thành chuỗi
    }


    public String getEmailFromToken(String jwt) {
        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }

        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

        return claims.get("email", String.class);
    }
}
