package com.playvoice.commentservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("security filter chain  >>>>>> ");

        http
//                .cors(Customizer.withDefaults()) // ✅ CORS 활성화
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(restAuthenticationEntryPoint)
                                .accessDeniedHandler(restAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth ->
                                auth
                                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                                        .permitAll()
                                        .requestMatchers("/comments/**")
//                                        .hasAnyAuthority("STUDENT", "MANAGER")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.PATCH, "/comments/*/reaction").permitAll()
                                        .anyRequest()
                                        .authenticated()
                )

                // 기존 JWT 검증 필터 대신, Gateway가 전달한 헤더를 이용하는 필터 추가
                .addFilterBefore(headerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public HeaderAuthenticationFilter headerAuthenticationFilter() {
        return new HeaderAuthenticationFilter();
    }

    // ✅ CORS 설정 빈 등록
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000")); // 프론트엔드 개발 주소
        config.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
        config.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
        config.setAllowCredentials(true); // 인증 정보 허용 (Authorization 헤더 등)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 전체 경로에 대해 적용

        return source;
    }
}
