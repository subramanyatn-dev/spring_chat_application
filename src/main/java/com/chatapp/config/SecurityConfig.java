package com.chatapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // For development only - enable in production
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable())
            )
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/ws/**", "/app/**", "/topic/**", "/user/**").permitAll()
                .requestMatchers("/**").permitAll()  // Allow access to static resources
                .anyRequest().permitAll()
            );

        return http.build();
    }
}