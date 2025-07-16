package com.playvoice.gatewayservice.filter;

import com.playvoice.gatewayservice.jwt.GatewayJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final GatewayJwtTokenProvider gatewayJwtTokenProvider;

    // 인증이 필요하지 않은 경로들
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
        "/auth/login",
        "/auth/signup", 
        "/auth/refresh",
        "/health",
        "/actuator/health"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        // 인증이 필요하지 않은 경로는 필터를 건너뜀
        if (isExcludedPath(path)) {
            log.info("Excluded path from JWT filter: {}", path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("no access Token for path: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!gatewayJwtTokenProvider.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Long userId = gatewayJwtTokenProvider.getUserIdFromJWT(token);
        String role = gatewayJwtTokenProvider.getRoleFromJWT(token);

        log.info("userId = {}", userId);
        log.info("role = {}", role);

        ServerHttpRequest mutateRequest = exchange.getRequest().mutate()
            .header("X-User-Id", String.valueOf(userId))
            .header("X-User-Role", role)
            .build();

        ServerWebExchange mutatedExchange = exchange.mutate().request(mutateRequest).build();

        return chain.filter(mutatedExchange);
    }

    private boolean isExcludedPath(String path) {
        // /uploads/로 시작하는 모든 경로를 예외 처리
        if (path.startsWith("/uploads/")) return true;
        return EXCLUDED_PATHS.stream().anyMatch(path::endsWith);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
