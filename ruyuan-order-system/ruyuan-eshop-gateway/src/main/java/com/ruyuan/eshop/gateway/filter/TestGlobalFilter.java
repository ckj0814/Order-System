package com.ruyuan.eshop.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义全局过滤器
 */
@Slf4j
@Component
public class TestGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 提取请求令牌
        String token = exchange.getRequest().getHeaders().getFirst("token");

        log.info("TestGlobalFilter");

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}