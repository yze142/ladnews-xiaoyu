package com.heima.app.gateway.fiter;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.heima.app.gateway.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @program: heima-leadnews
 * @description: 统一鉴权类
 * @author: hello.xaioyu
 * @create: 2022-06-02 22:10
 **/

@Component
@Slf4j
public class AuthorizeFilter implements Ordered, GlobalFilter {

    @Value("${Filter.url}")
    private List list;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //判断传递过来的url是否需要鉴权
        if (request.getURI().getPath().contains("/login")) {
            //无需鉴权直接放行
            //放行
            return chain.filter(exchange);
        }

        //判断token是否存在
        String token = request.getHeaders().getFirst("token");

        //4.判断token是否存在
        if (StringUtils.isBlank(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);//返回一个状态吗回去
            return response.setComplete();

        }
        try {


            //判断token是否过期
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            int i = AppJwtUtil.verifyToken(claimsBody);
            if (i == 1 || i == 2) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //放行
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
