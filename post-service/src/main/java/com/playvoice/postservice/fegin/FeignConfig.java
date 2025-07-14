package com.playvoice.postservice.fegin;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@Slf4j
public class FeignConfig {

    @Bean
    public RequestInterceptor tokenRelayInterceptor() {
        return requestTemplate -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                String authorizationHeader = request.getHeader("Authorization");
                log.info("엑세스 토큰{}", authorizationHeader);
                if (authorizationHeader != null) {
                    requestTemplate.header("Authorization", authorizationHeader);
                }

                // 추가: X-User-Id, X-User-Role 헤더 전달
                String userId = request.getHeader("X-User-Id");
                String role = request.getHeader("X-User-Role");
                log.info("유저 ID: {}, 역할: {}", userId, role);
                if (userId != null) {
                    requestTemplate.header("X-User-Id", userId);
                }
                if (role != null) {
                    requestTemplate.header("X-User-Role", role);
                }
            }
        };
    }
}