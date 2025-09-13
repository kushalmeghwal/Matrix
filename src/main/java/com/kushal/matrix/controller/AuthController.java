package com.kushal.matrix.controller;

import com.kushal.matrix.dtos.AuthResponse;
import com.kushal.matrix.dtos.LoginRequest;
import com.kushal.matrix.dtos.RegisterRequest;
import com.kushal.matrix.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping("/")
    public String hello(){
        try {
            return "hello world";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
}
