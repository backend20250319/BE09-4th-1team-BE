package com.playvoice.gatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CorsFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();

        // CORS 헤더가 이미 설정되어 있으면 제거
        headers.remove("Access-Control-Allow-Origin");
        headers.remove("Access-Control-Allow-Methods");
        headers.remove("Access-Control-Allow-Headers");
        headers.remove("Access-Control-Allow-Credentials");
        headers.remove("Access-Control-Max-Age");

        // 새로운 CORS 헤더 설정 (중복 방지를 위해 set 사용)
        headers.set("Access-Control-Allow-Origin", "http://localhost:3000");
        headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
        headers.set("Access-Control-Allow-Headers", "*");
        headers.set("Access-Control-Allow-Credentials", "true");
        headers.set("Access-Control-Max-Age", "3600");

        // OPTIONS 요청 처리
        if (exchange.getRequest().getMethod().name().equals("OPTIONS")) {
            response.setStatusCode(org.springframework.http.HttpStatus.OK);
            return Mono.empty();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -2; // JWT 필터보다 먼저 실행
    }
}