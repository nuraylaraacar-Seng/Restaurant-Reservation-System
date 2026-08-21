package com.reservation.restaurant_reservation.infrastructure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            // 1. Durum: OAuth2 JWT Nesnesi ise
            if (principal instanceof Jwt jwt) {
                // Eğer userId JWT'nin subject alanındaysa (sub):
                if (jwt.getSubject() != null) {
                    try {
                        return Long.parseLong(jwt.getSubject());
                    } catch (NumberFormatException ignored) {}
                }

                Object userIdClaim = jwt.getClaim("userId");
                if (userIdClaim == null) {
                    userIdClaim = jwt.getClaim("id");
                }
                if (userIdClaim instanceof Number number) {
                    return number.longValue();
                } else if (userIdClaim instanceof String str) {
                    return Long.parseLong(str);
                }
            }

            // 2. Durum: Principal doğrudan String ise
            if (principal instanceof String str && !"anonymousUser".equals(str)) {
                try {
                    return Long.parseLong(str);
                } catch (NumberFormatException ignored) {}
            }

            // 3. Durum: authentication.getName() üzerinden ID çözme
            if (authentication.getName() != null && !"anonymousUser".equals(authentication.getName())) {
                try {
                    return Long.parseLong(authentication.getName());
                } catch (NumberFormatException ignored) {}
            }
        }

        throw new IllegalStateException("Kullanıcı kimliği doğrulanamadı!");
    }
}
