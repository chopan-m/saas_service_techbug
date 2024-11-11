package com.api_gateway.filter;

import com.api_gateway.dto.GenericResponse;
import com.api_gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {

        System.out.println("============== Apply Method Start ==============");
        GatewayFilter bearer =  ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {

                System.out.println(".................1");
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                   throw new RuntimeException("missing authorization header while accessing path -> " + exchange.getRequest().getPath().toString());
//                    Object ResponseEntity;
//                    return "Authorization token not provided. Please include a valid token.";
                    return onError(exchange,
                                exchange.getRequest().getPath().toString(),
                                "Authorization token not provided or invalid while accessing path -> "+ exchange.getRequest().getPath(),
                                HttpStatus.UNAUTHORIZED);
                }


                System.out.println(".................2");
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                System.out.println(".................3");
                try {
                    // i) do a rest call to validate the token from auth-service using restclient
                    // or 2) copy the validation code from auth-service to jwtutil class..
                    // Approach # 2 is used..
                     jwtUtil.validateToken(authHeader);

                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            }

//            System.out.println("============== Apply Method End ==============");
            return chain.filter(exchange);
        });
        System.out.println("============== Returning Bearer ==============");
        return bearer;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String path, String error, HttpStatus httpStatus) {
        // Set the response status code and body
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // Custom error message
        String errorMessage = String.format("{\"error\": \"%s\", \"message\": \"%s\"}", httpStatus.getReasonPhrase(), error);

        // Write error message to response
        byte[] bytes = errorMessage.getBytes(StandardCharsets.UTF_8);
//        System.out.println("bytes .........");
//        System.out.println(bytes);

        GenericResponse<DataBuffer> responseBody =  GenericResponse.build(path, HttpStatus.OK.value(),true, "Success", "", exchange.getResponse().bufferFactory().wrap(bytes));
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
////        GenericResponse<Mono<Void>> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.OK.value(),
////                true, "Success", "", entity);
//        Mono<Void> mono = Mono.fromRunnable(() -> {
//            // Perform the action and capture the result
////            String result = performAction();
//            // Perform side effects such as logging
////            System.out.println("Result from custom service: " + result);
//            GenericResponse<DataBuffer> responseBody1 = responseBody;
//            return responseBody
//        });
//        return mono;
    }

    public static class Config {
        public Config() {
            System.out.println("Config() -> | Create Object Config class of sub class AuthenticationFilter extends by AbstractGatewayFilterFactory");
        }
    }
}
