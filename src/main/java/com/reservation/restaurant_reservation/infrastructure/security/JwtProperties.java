package com.reservation.restaurant_reservation.infrastructure.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private Rsa rsa = new Rsa();
    private long expirationMs = 86400000L;

    @Getter
    @Setter
    public static class Rsa {
        private String privateKeyLocation;
        private String publicKeyLocation;
    }
}
