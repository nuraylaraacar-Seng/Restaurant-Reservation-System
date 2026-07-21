package com.reservation.restaurant_reservation.infrastructure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            return Long.parseLong((String) authentication.getPrincipal());
        }
        throw new IllegalStateException("Kullanıcı kimliği doğrulanamadı!");
    }
}