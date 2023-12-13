package com.cos.jwt.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// /login을 통해 username과 password를 post로 전송하면 UsernamePasswordAuthenticationFilter가 실행된다
// 근데 지금은 config에서 formlogin을 disable 했기 때문에 실행이 안되고, PrincipalDetailsService을 위해 여기서
// 상속받고 다시 작성
// AuthenticationManager를 통해 로그인이 진행
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    //로그인 요청을 하면, 로그인 시도를 위해 실행되는 메소드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("로그인 시도중");

        //1. 넘어온 username과 pw를 받아서

        //2. 정상적인 로그인 시도인지 확인 --> authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출

        //3. PrincipalDetails를 세션에 담고--> 권한 관리를 위해 요청 응답 시에만 세션에 임시로

        //4. JWT 토큰 생성 후 응답.
        return super.attemptAuthentication(request, response);
    }
}
