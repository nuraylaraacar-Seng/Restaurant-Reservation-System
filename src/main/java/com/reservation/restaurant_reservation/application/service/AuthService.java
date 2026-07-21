package com.reservation.restaurant_reservation.application.service;

import com.reservation.restaurant_reservation.application.dto.request.LoginRequest;
import com.reservation.restaurant_reservation.application.dto.request.RegisterRequest;
import com.reservation.restaurant_reservation.application.dto.response.AuthResponse;
import com.reservation.restaurant_reservation.domain.entity.User;
import com.reservation.restaurant_reservation.domain.enums.Role;
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
    private final PasswordEncoder passwordEncoder; // Şifreleri güvenle şifrelemek için yaptım
    private final JwtEncoder jwtEncoder;           // JWT üretmek için

    public String register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // ŞİFRE ŞİFRELENDİ!
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);
        return "Kayıt başarılı: " + request.getEmail();
    }


    public AuthResponse login(LoginRequest request) {
        // 1. Kullanıcıyı bul
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı veya şifre hatalı."));

        // 2. Şifreyi kontrol et
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Kullanıcı veya şifre hatalı.");
        }

        // 3. Gerçek bir JWT Token üretir
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("restaurant-app")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600)) // 1 saat geçerli
                .subject(String.valueOf(user.getId()))
                .claim("role", user.getRole().name())
                .build();


        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        // 4. Token'ı ve diğer bilgileri döndüren yapı
        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .email(user.getEmail())
                .role(user.getRole())
                .userId(user.getId())
                .build();
    }
}