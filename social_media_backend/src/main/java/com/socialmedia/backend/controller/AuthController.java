package com.socialmedia.backend.controller;

import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.model.User;
import com.socialmedia.backend.repository.UserRepository;
import com.socialmedia.backend.response.AuthResponse;
import com.socialmedia.backend.security.JwtProvider;
import com.socialmedia.backend.service.CustomUserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CustomUserDetailsServiceImplementation customUserDetails;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String brithDate = user.getBirthDate();

        Optional<User> isEmailExits = userRepository.findByEmail(email);
        if (isEmailExits.isPresent()) {
            throw new UserException("Email is already in use by another user");
        }

        return null;
    }

}
