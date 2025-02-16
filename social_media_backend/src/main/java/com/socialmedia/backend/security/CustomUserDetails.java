package com.socialmedia.backend.security;

import com.socialmedia.backend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Chưa có phân quyền, nên để danh sách rỗng
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Chưa xử lý logic khóa tài khoản
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Chưa xử lý logic khóa tài khoản
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Chưa xử lý logic hết hạn mật khẩu
    }

    @Override
    public boolean isEnabled() {
        return true; // Mặc định tài khoản luôn kích hoạt
    }

    public boolean isLoginWithGoogle() {
        return user.isLoginWithGoogle();
    }
}
