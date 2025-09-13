package com.kushal.matrix.service;

import com.kushal.matrix.dtos.AuthResponse;
import com.kushal.matrix.dtos.LoginRequest;
import com.kushal.matrix.dtos.RegisterRequest;
import com.kushal.matrix.model.User;
import com.kushal.matrix.repository.UserRepository;
import com.kushal.matrix.security.jwt.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request){
        User user= User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        String token=jwtUtils.generateToken(user);
        return new AuthResponse(token,user.getRole().name());
    }

    public AuthResponse login(LoginRequest request){
        User user=userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("User not found"));
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("Wrong password");
        }
        String token = jwtUtils.generateToken(user);
        return new AuthResponse(token,user.getRole().name());
    }
}
