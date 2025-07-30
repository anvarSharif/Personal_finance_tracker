package com.example.personal_finance_tracker.controller;

import com.example.personal_finance_tracker.dto.LoginDTO;
import com.example.personal_finance_tracker.dto.RegisterDTO;
import com.example.personal_finance_tracker.security.AuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDTO loginDTO){
       return ResponseEntity.ok(authService.handleLogin(loginDTO));
    }
    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody RegisterDTO registerDTO){
       return ResponseEntity.ok(authService.saveUser(registerDTO));
    }

}
