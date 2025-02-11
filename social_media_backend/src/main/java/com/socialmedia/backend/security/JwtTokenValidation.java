package com.socialmedia.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidation extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Lấy JWT từ header với tên định danh từ JwtConstant.JWT_HEADER
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        // Kiểm tra xem token có tồn tại và có tiền tố "Bearer " hay không
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Bỏ tiền tố "Bearer "

            try {
                // Giải mã khóa bí mật từ base64
                SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtConstant.SECRET_KEY));

                // Phân tích token để lấy Claims (payload của JWT)
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                // Lấy thông tin email từ token
                String email = claims.get("email", String.class);

                // Lấy danh sách quyền từ token
                String authorities = claims.get("authorities", String.class);
                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // Tạo đối tượng Authentication với thông tin từ token
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);

                // Đặt thông tin Authentication vào SecurityContext của Spring Security
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Trả về lỗi HTTP 401 nếu JWT không hợp lệ hoặc đã hết hạn
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\": \"JWT không hợp lệ hoặc đã hết hạn\"}");

                return;
            }
        }

        // Tiếp tục thực hiện các filter khác nếu không có lỗi
        filterChain.doFilter(request, response);
    }
}
