package com.socialmedia.backend.controller;

import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.entities.Verification;
import com.socialmedia.backend.repository.UserRepository;
import com.socialmedia.backend.response.AuthResponse;
import com.socialmedia.backend.security.JwtProvider;
import com.socialmedia.backend.service.CustomUserDetailsServiceImplementation;
import com.socialmedia.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsServiceImplementation customUserDetails;
    private final UserService userService;
    private static Map<String, User> sessionData = new HashMap<>();

    @Autowired
    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            CustomUserDetailsServiceImplementation customUserDetails, UserService userService) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetails = customUserDetails;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        System.out.println("Received Signup Request: " + user); // Debug đầu vào

        String email = user.getEmail();
        if (email == null || email.isBlank()) {
            throw new UserException("Email cannot be empty");
        }

        System.out.println("Checking if email exists: " + email);
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException("Email is already in use by another user");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        System.out.println("Encoded Password: " + encodedPassword);

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(user.getFullName());
        createdUser.setPassword(encodedPassword);
        createdUser.setBirthDate(user.getBirthDate());
        createdUser.setVerification(new Verification());

        User savedUser = userRepository.save(createdUser);
        System.out.println("User saved: " + savedUser);

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
