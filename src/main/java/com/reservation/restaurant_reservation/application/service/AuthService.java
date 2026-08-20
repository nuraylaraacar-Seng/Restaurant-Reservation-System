package com.reservation.restaurant_reservation.application.service;

import com.reservation.restaurant_reservation.application.dto.request.LoginRequest;
import com.reservation.restaurant_reservation.application.dto.request.RegisterRequest;
import com.reservation.restaurant_reservation.application.dto.response.AuthResponse;
import com.reservation.restaurant_reservation.domain.entity.User;
import com.reservation.restaurant_reservation.domain.enums.Role;
import com.reservation.restaurant_reservation.domain.exception.DuplicateResourceException;
import com.reservation.restaurant_reservation.infrastructure.persistence.repository.UserRepository;
import com.reservation.restaurant_reservation.domain.exception.ResourceNotFoundException;
import com.reservation.restaurant_reservation.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Bu email adresi zaten kayıtlı.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.CUSTOMER)
                .build();

        User savedUser = userRepository.save(user);
        return buildAuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı veya şifre hatalı."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Kullanıcı veya şifre hatalı.");
        }

        return buildAuthResponse(user);
    }

    // register ve login aynı token üretim mantığını paylaşıyordu, tek yere topladım
    private AuthResponse buildAuthResponse(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("restaurant-app")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(String.valueOf(user.getId()))
                .claim("role", user.getRole().name())
                .build();

        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .email(user.getEmail())
                .role(user.getRole())
                .userId(user.getId())
                .build();
    }
}
