package com.socialmedia.backend.controller;

import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.model.User;
import com.socialmedia.backend.model.Verification;
import com.socialmedia.backend.repository.UserRepository;
import com.socialmedia.backend.response.AuthResponse;
import com.socialmedia.backend.security.JwtProvider;
import com.socialmedia.backend.service.CustomUserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsServiceImplementation customUserDetails;

    @Autowired
    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            CustomUserDetailsServiceImplementation customUserDetails) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetails = customUserDetails;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String birthDate = user.getBirthDate();

        if (email == null || email.isBlank()) {
            throw new UserException("Email cannot be empty");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException("Email is already in use by another user");
        }

        // Mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(password);

        // Tạo user mới
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setPassword(encodedPassword);
        createdUser.setBirthDate(birthDate);
        createdUser.setVerification(new Verification());

        // Lưu vào database
        User savedUser = userRepository.save(createdUser);

        // Tạo JWT Token
        String token = jwtProvider.generateJwt(savedUser.getEmail());

        return new ResponseEntity<>(new AuthResponse(token, true), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody User user) throws UserException {
        String username = user.getEmail();
        String password = user.getPassword();

        // Xác thực tài khoản
        Authentication authentication = authenticate(username, password);

        // Tạo JWT bằng email
        String token = jwtProvider.generateJwt(username);

        return new ResponseEntity<>(new AuthResponse(token, true), HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) throws UserException {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        if (userDetails == null) {
            throw new UserException("User not found with email: " + username);
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password or username");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
