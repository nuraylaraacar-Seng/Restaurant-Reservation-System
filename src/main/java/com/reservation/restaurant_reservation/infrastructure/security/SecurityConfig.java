package com.reservation.restaurant_reservation.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // JWT kullandığımız için CSRF'e ihtiyaç yok.
                .csrf(csrf -> csrf.disable())

                // Session oluşturma, tamamen JWT ile çalış.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // Herkes erişebilir
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/tables-dashboard"
                        ).permitAll()

                        // Masaları görüntülemek serbest
                        .requestMatchers(HttpMethod.GET, "/api/tables")
                        .permitAll()

                        // Masa oluşturmak giriş gerektirir
                        .requestMatchers(HttpMethod.POST, "/api/tables")
                        .authenticated()

                        // Güncellemek giriş gerektirir
                        .requestMatchers(HttpMethod.PUT, "/api/tables/**")
                        .authenticated()

                        // Silmek için giriş gerektirir
                        .requestMatchers(HttpMethod.DELETE, "/api/tables/**")
                        .authenticated()

                        // Geri kalan her endpoint token ister.
                        .anyRequest()
                        .authenticated()
                )

                // JWT doğrulama
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                );

        return http.build();
    }
}