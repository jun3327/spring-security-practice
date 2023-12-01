package com.cos.security1.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록된다.
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf 비활성화 이유: 개발환경에서 postman 등 api로 연습할 때엔 클라이언트가 csrf token 없이 보내기 때문에
        //비활성화 해야 서버가 데이터를 반환할 수 있다.
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorize ->
                authorize
                        //url에 따라 인가된 사용자만 접근가능하게 설정
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")

                        //위 url 외 모든 요청은 아무 사용자에게나 다 허용.
                        .anyRequest().permitAll()
                        )
                        //권한 없는 사용자가 요청했을 경우, JSON으로 반환(CSR)
//                        .exceptionHandling((exceptionHandling) ->
//                                exceptionHandling
//                                    .authenticationEntryPoint((request, response, authException) -> {
//                                        // 권한이 없는 경우 JSON 형태로 응답
//                                        response.setContentType("application/json;charset=UTF-8");
//                                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                                        response.getWriter().write("{\"message\":\"로그인필요\",\"error\":true}");
//                                    })
//                        );
//
//                        //권한 없는 사용자가 요청했을 경우, 로그인 페이지로 가도록 설정(SSR)
                        .formLogin(formLogin -> formLogin
                                .loginPage("/loginForm")
                        );


        return http.build();
    }
}
