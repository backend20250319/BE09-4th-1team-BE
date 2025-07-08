package com.playvoice.discoveryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/eureka/**", "/").permitAll() // Eureka 대시보드와 API 모두 인증 없이 허용
                .anyRequest().permitAll()
            .and()
            .httpBasic().disable()
            .formLogin().disable();
        return http.build();
    }
} 