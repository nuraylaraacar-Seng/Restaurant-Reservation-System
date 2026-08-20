package com.reservation.restaurant_reservation.presentation.advice.controller;

import com.reservation.restaurant_reservation.application.service.AuthService;
import com.reservation.restaurant_reservation.application.dto.request.LoginRequest;
import com.reservation.restaurant_reservation.application.dto.request.RegisterRequest;
import com.reservation.restaurant_reservation.application.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}